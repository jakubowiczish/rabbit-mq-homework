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
import static system.enums.ExchangeTypes.SPACE_AGENCY_EXCHANGE;
import static system.enums.ServiceType.printAllAvailableTypesOfServices;
import static system.util.ColouredPrinter.printlnColoured;
import static system.util.ConsoleColor.BLUE_BOLD_BRIGHT;
import static system.util.Utils.*;

@Slf4j
public class SpaceAgency {

    private final String name;
    private final Channel regularChannel;

    private long requestId;

    public SpaceAgency(String name) {
        this.name = name;
        this.regularChannel = createRegularChannel();
        createAdminChannelForUsers(AGENCIES.getName());
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
            String requestToSend = createRequest(serviceType);

            regularChannel.basicPublish(
                    SPACE_AGENCY_EXCHANGE.getName(),
                    serviceType,
                    properties,
                    requestToSend.getBytes(StandardCharsets.UTF_8));

            printlnColoured("REQUEST SENT: " + requestToSend, BLUE_BOLD_BRIGHT);

            incrementRequestId();
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

    private String createRequest(String serviceType) {
        return "Request: [" + requestId + "]" + " from space agency " + name + " for " + serviceType;
    }

    private void incrementRequestId() {
        requestId += 1;
    }
}
