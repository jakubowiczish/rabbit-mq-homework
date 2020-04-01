package system.carrier;

import com.rabbitmq.client.*;
import lombok.SneakyThrows;
import system.enums.ServiceType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static system.enums.ExchangeTypes.SPACE_AGENCY_EXCHANGE;
import static system.enums.ServiceType.printAllAvailableTypesOfServices;
import static system.util.Utils.createDefaultChannel;
import static system.util.Utils.stop;

public class Carrier {

    private final String name;
    private final Channel regularChannel;

    public Carrier(String name) {
        this.name = name;
        this.regularChannel = createInitializedChannel();
    }

    public void start() {
        System.out.println("Waiting for requests...");
    }

    @SneakyThrows
    private Channel createInitializedChannel() {
        Channel channel = createDefaultChannel();
        channel.basicQos(0);
        channel.exchangeDeclare(SPACE_AGENCY_EXCHANGE.getName(), BuiltinExchangeType.TOPIC);

        String[] typesOfServices = chooseTypesOfServices();
        String firstTypeOfService = ServiceType.fromString(typesOfServices[0]).getName();
        String secondTypeOfService = ServiceType.fromString(typesOfServices[1]).getName();

        initQueue(channel, firstTypeOfService);
        initQueue(channel, secondTypeOfService);

        Consumer consumer = createConsumerForChannel();
        channel.basicConsume(firstTypeOfService, false, consumer);
        channel.basicConsume(secondTypeOfService, false, consumer);
        return channel;
    }

    @SneakyThrows
    private String[] chooseTypesOfServices() {
        printAllAvailableTypesOfServices("Choose 2 types of services from available: ");

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String first = br.readLine();
        if ("exit".equals(first)) stop("Exit requested...");
        System.out.println("Choose once more");
        String second = br.readLine();
        if ("exit".equals(second)) stop("Exit requested...");

        if (Objects.equals(first, second)) {
            System.out.println("You have to choose 2 different types of services");
            chooseTypesOfServices();
        }

        return new String[]{first, second};
    }

    @SneakyThrows
    private void initQueue(Channel channel, String service) {
        channel.queueDeclare(service, false, false, false, null);
        channel.queueBind(service, SPACE_AGENCY_EXCHANGE.getName(), service);
    }

    private Consumer createConsumerForChannel() {
        return new DefaultConsumer(regularChannel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, StandardCharsets.UTF_8);
                System.out.println("Message received: " + message);
                System.out.println("Request " + properties.getCorrelationId()
                        + " for " + properties.getReplyTo()
                        + " received. Task performed successfully.");

                String response = "Task with request id number : " + properties.getCorrelationId() +
                        " has been performed successfully by " + name;

                regularChannel.basicPublish(
                        SPACE_AGENCY_EXCHANGE.getName(),
                        properties.getReplyTo(),
                        properties,
                        response.getBytes(StandardCharsets.UTF_8));
                regularChannel.basicAck(envelope.getDeliveryTag(), false);
            }
        };
    }
}
