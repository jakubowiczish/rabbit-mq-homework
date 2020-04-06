package system.util;

import com.rabbitmq.client.*;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import static system.enums.ExchangeTypes.ADMIN_EXCHANGE;
import static system.util.ColouredPrinter.printColoured;
import static system.util.ColouredPrinter.printlnColoured;
import static system.util.ConsoleColor.*;

public class Utils {

    private static final String HOST_NAME = "localhost";

    public static void stop(String reason) {
        printlnColoured(reason, ConsoleColor.RED_BOLD_BRIGHT);
        System.exit(0);
    }

    public static String chooseName(String asWho) {
        printlnColoured("Choose your name as " + asWho + ": ", BLUE_BOLD_BRIGHT);
        String name = getFirstReadLine();
        printColoured("Your name is: ", CYAN_BOLD_BRIGHT);
        printlnColoured(name, RED_UNDERLINED);
        return name;
    }

    @SneakyThrows
    public static void createAdminChannelForUsers(String queueName) {
        Channel channel = createDefaultChannel();
        channel.exchangeDeclare(ADMIN_EXCHANGE.getName(), BuiltinExchangeType.TOPIC);
        String adminQueue = channel.queueDeclare().getQueue();
        channel.queueBind(adminQueue, ADMIN_EXCHANGE.getName(), queueName);
        Consumer consumer = createDefaultConsumer(channel, "MESSAGE FROM THE ADMIN: ");
        channel.basicConsume(adminQueue, false, consumer);
    }

    public static Channel createDefaultChannel() {
        return createChannel();
    }

    public static Consumer createDefaultConsumer(Channel channel, String messagePrefix) {
        return new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, StandardCharsets.UTF_8);
                printlnColoured(messagePrefix + message, RED_BOLD_BRIGHT);
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        };
    }

    @SneakyThrows
    public static String getFirstReadLine() {
        return new BufferedReader(new InputStreamReader(System.in)).readLine();
    }

    @SneakyThrows
    private static Channel createChannel() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOST_NAME);
        return factory.newConnection().createChannel();
    }
}
