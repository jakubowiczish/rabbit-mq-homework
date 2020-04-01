package system.util;

import com.rabbitmq.client.*;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class Utils {

    private static final String HOST_NAME = "localhost";

    public static void stop(String reason) {
        System.out.println(reason);
        System.exit(0);
    }

    public static Channel createDefaultChannel() {
        return createChannel();
    }

    public static Consumer createDefaultConsumer(Channel channel, String messagePrefix) {
        return new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, StandardCharsets.UTF_8);
                System.out.println(messagePrefix + message);
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
