package system.space_agency;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Consumer;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import system.enums.ServiceType;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import static system.enums.AdminServiceType.AGENCIES;
import static system.enums.ExchangeTypes.ADMIN_EXCHANGE;
import static system.enums.ExchangeTypes.SPACE_AGENCY_EXCHANGE;
import static system.enums.ServiceType.printAllAvailableTypesOfServices;
import static system.util.Utils.*;

@Slf4j
public class SpaceAgency {

    private final String name;
    private final Channel regularChannel;
    private final Channel adminChannel;

    private long requestId;

    public SpaceAgency(String name) {
        this.name = name;
        this.regularChannel = createRegularChannel();
        this.adminChannel = createAdminChannel();
    }

    private static String createAgencyRoutingKey(String name) {
        return "AGENCY." + name;
    }

    @SneakyThrows
    public void start() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            printAllAvailableTypesOfServices("Choose type of service from available: ");

            String input = br.readLine();
            if ("exit".equals(input)) stop("Exit requested...");

            String serviceType = ServiceType.fromString(input).getName();
            AMQP.BasicProperties properties = createProperties();
            String messageToSend = createMessageToSend(serviceType);

            regularChannel.basicPublish(
                    SPACE_AGENCY_EXCHANGE.getName(),
                    serviceType,
                    properties,
                    messageToSend.getBytes(StandardCharsets.UTF_8));
            requestId += 1;
        }
    }

    @SneakyThrows
    private Channel createRegularChannel() {
        Channel channel = createDefaultChannel();
        channel.exchangeDeclare(SPACE_AGENCY_EXCHANGE.getName(), BuiltinExchangeType.TOPIC);
        channel.queueDeclare(name, false, false, false, null);
        channel.queueBind(name, SPACE_AGENCY_EXCHANGE.getName(), createAgencyRoutingKey(name));
        Consumer consumer = createDefaultConsumer(channel, "");
        channel.basicConsume(name, false, consumer);
        return channel;
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

    @SneakyThrows
    private Channel createAdminChannel() {
        Channel channel = createDefaultChannel();
        channel.exchangeDeclare(ADMIN_EXCHANGE.getName(), BuiltinExchangeType.TOPIC);
        String adminQueue = channel.queueDeclare().getQueue();
        channel.queueBind(adminQueue, ADMIN_EXCHANGE.getName(), AGENCIES.getName());
        Consumer consumer = createDefaultConsumer(channel, "");
        channel.basicConsume(adminQueue, false, consumer);
        return channel;
    }
}
