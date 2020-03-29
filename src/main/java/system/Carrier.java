package system;

import com.rabbitmq.client.*;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static system.ChannelUtil.*;
import static system.ServiceType.printAllAvailableTypesOfServices;
import static system.Utils.createDefaultChannel;

public class Carrier {

    public static final String CARRIER_EXCHANGE = "CARRIER_EXCHANGE";

    private final String name;
    private final Channel channel;

    public Carrier(String name) {
        this.name = name;
        this.channel = createInitializedChannel();
    }

    public void start() {

    }

    @SneakyThrows
    private Channel createInitializedChannel() {
        Channel channel = createDefaultChannel();
        channel.basicQos(0);
        channel.exchangeDeclare(CARRIER_EXCHANGE, BuiltinExchangeType.TOPIC);

        String[] typesOfServices = chooseTypesOfServices();
        String firstTypeOfService = ServiceType.fromString(typesOfServices[0]).toString();
        String secondTypeOfService = ServiceType.fromString(typesOfServices[1]).toString();

        initQueue(channel, firstTypeOfService);
        initQueue(channel, secondTypeOfService);

        Consumer consumer = createConsumerForChannel();
        consume(channel, name, consumer);
        return channel;
    }

    @SneakyThrows
    private String[] chooseTypesOfServices() {
        printAllAvailableTypesOfServices("Choose 2 types of services from available: ");

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String first = br.readLine();
        System.out.println("Choose once more");
        String second = br.readLine();

        if ("exit".equals(first) || "exit".equals(second)) stop("Exit requested...");

        if (Objects.equals(first, second)) {
            System.out.println("You have to choose 2 different types of services");
            chooseTypesOfServices();
        }

        return new String[]{first, second};
    }

    @SneakyThrows
    private void initQueue(Channel channel, String service) {
        channel.queueDeclare(service, false, false, false, null);
        channel.queueBind(service, CARRIER_EXCHANGE, createCarrierRoutingKey(service));
    }

    private Consumer createConsumerForChannel() {
        return new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, StandardCharsets.UTF_8);
                System.out.println("Message received: " + message);
                System.out.println("Request " + properties.getCorrelationId()
                        + " for " + properties.getReplyTo()
                        + " received. Task performed successfully.");

                String response = "Task with request id number : " + properties.getCorrelationId() +
                        " has been performed successfully by " + name;

                channel.basicPublish(
                        CARRIER_EXCHANGE,
                        properties.getReplyTo(),
                        properties,
                        response.getBytes(StandardCharsets.UTF_8));
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        };
    }

    private static String createCarrierRoutingKey(String name) {
        return "CARRIER#" + name;
    }

}
