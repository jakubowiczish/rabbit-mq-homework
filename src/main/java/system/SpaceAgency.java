package system;

import com.rabbitmq.client.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import static system.ChannelUtil.*;
import static system.ServiceType.printAllAvailableTypesOfServices;
import static system.Utils.createDefaultChannel;

@Slf4j
public class SpaceAgency {

    public static final String SPACE_AGENCY_EXCHANGE = "SPACE_AGENCY_EXCHANGE";

    private final String name;
    private final Channel channel;

    private long requestId;

    public SpaceAgency(String name) {
        this.name = name;
        this.channel = createInitializedChannel();
    }

    @SneakyThrows
    public void start() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            printAllAvailableTypesOfServices("Choose type of service from available: ");

            String input = br.readLine();
            if ("exit".equals(input)) stop("Exit requested...");

            String serviceType = ServiceType.fromString(input).toString();
            AMQP.BasicProperties properties = createProperties();
            String messageToSend = createMessageToSend(serviceType);

            channel.basicPublish(
                    SPACE_AGENCY_EXCHANGE,
                    serviceType,
                    properties,
                    messageToSend.getBytes(StandardCharsets.UTF_8));
            requestId += 1;
        }
    }

    @SneakyThrows
    private Channel createInitializedChannel() {
        Channel channel = createDefaultChannel();
        channel.exchangeDeclare(SPACE_AGENCY_EXCHANGE, BuiltinExchangeType.TOPIC);
        initQueue(channel, name);

        Consumer consumer = createConsumerForChannel();
        consume(channel, name, consumer);
        return channel;
    }

    @SneakyThrows
    private void initQueue(Channel channel, String name) {
        channel.queueDeclare(name, false, false, false, null);
        channel.queueBind(name, SPACE_AGENCY_EXCHANGE, createAgencyRoutingKey(name));
    }

    public Consumer createConsumerForChannel() {
        return new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, StandardCharsets.UTF_8);
                System.out.println(message);
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        };
    }

    private AMQP.BasicProperties createProperties() {
        return new AMQP.BasicProperties
                .Builder()
                .correlationId(String.valueOf(requestId))
                .replyTo(createAgencyRoutingKey(name))
                .build();
    }

    private String createMessageToSend(String serviceType) {
        return "Request number: " + requestId + " from " + name + " for " + serviceType;
    }

    private static String createAgencyRoutingKey(String name) {
        return "AGENCY#" + name;
    }
}
