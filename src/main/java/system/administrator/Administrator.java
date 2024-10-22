package system.administrator;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Consumer;
import lombok.SneakyThrows;
import system.enums.AdminServiceType;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import static system.enums.AdminServiceType.printAllAvailableTypesOfServices;
import static system.enums.ExchangeTypes.ADMIN_EXCHANGE;
import static system.enums.ExchangeTypes.SPACE_AGENCY_EXCHANGE;
import static system.util.ColouredPrinter.printlnColoured;
import static system.util.ConsoleColor.BLUE_BOLD_BRIGHT;
import static system.util.ConsoleColor.GREEN_BOLD_BRIGHT;
import static system.util.Utils.*;

public class Administrator {

    private static final String ADMIN_KEY = "#";

    private final String name;
    private final Channel adminChannel;

    public Administrator(String name) {
        this.name = name;
        createRegularChannel();
        this.adminChannel = createAdminChannel();
    }

    @SneakyThrows
    public void start() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            printAllAvailableTypesOfServices("Choose to whom you want to send a message: ");
            String input = br.readLine();

            if ("exit".equals(input)) stop("Exit requested...");

            String adminServiceType = AdminServiceType.fromString(input).getName();
            String messageToSend = createMessageToSend(br);

            adminChannel.basicPublish(
                    ADMIN_EXCHANGE.getName(),
                    adminServiceType,
                    null,
                    messageToSend.getBytes(StandardCharsets.UTF_8));

            printlnColoured("MESSAGE SENT: " + messageToSend, BLUE_BOLD_BRIGHT);
        }
    }

    @SneakyThrows
    private void createRegularChannel() {
        Channel channel = createDefaultChannel();
        channel.exchangeDeclare(SPACE_AGENCY_EXCHANGE.getName(), BuiltinExchangeType.TOPIC);
        channel.queueDeclare(name, false, false, false, null);
        channel.queueBind(name, SPACE_AGENCY_EXCHANGE.getName(), ADMIN_KEY);
        Consumer consumer = createDefaultConsumer(channel, "");
        channel.basicConsume(name, false, consumer);
    }

    @SneakyThrows
    private Channel createAdminChannel() {
        Channel channel = createDefaultChannel();
        channel.exchangeDeclare(ADMIN_EXCHANGE.getName(), BuiltinExchangeType.TOPIC);
        return channel;
    }

    @SneakyThrows
    private String createMessageToSend(BufferedReader br) {
        printlnColoured("Enter your message: ", GREEN_BOLD_BRIGHT);
        return br.readLine();
    }
}
