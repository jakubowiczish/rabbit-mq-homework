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
import static system.util.Utils.*;

public class Administrator {

    private final Channel regularChannel;
    private final Channel adminChannel;
    private String name;

    public Administrator(String name) {
        this.name = name;
        this.regularChannel = createRegularChannel();
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
        }
    }

    @SneakyThrows
    private Channel createRegularChannel() {
        Channel channel = createDefaultChannel();
        channel.exchangeDeclare(SPACE_AGENCY_EXCHANGE.getName(), BuiltinExchangeType.TOPIC);
        channel.queueDeclare(name, false, false, false, null);
        channel.queueBind(name, SPACE_AGENCY_EXCHANGE.getName(), createAdminKey());
        Consumer consumer = createDefaultConsumer(channel, "---");
        channel.basicConsume(name, false, consumer);
        return channel;
    }

    @SneakyThrows
    private Channel createAdminChannel() {
        Channel channel = createDefaultChannel();
        channel.exchangeDeclare(ADMIN_EXCHANGE.getName(), BuiltinExchangeType.TOPIC);
        return channel;
    }

    @SneakyThrows
    private String createMessageToSend(BufferedReader br) {
        System.out.println("Enter your message: ");
        return "ADMIN'S MESSAGE: " + br.readLine();
    }

    private String createAdminKey() {
        return "ADMIN." + name;
    }
}
