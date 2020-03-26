package system;

import com.rabbitmq.client.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeoutException;

import static system.ChannelUtils.*;
import static system.ServiceType.chooseProvidedServices;

public class Carrier {

    public static void main(String[] args) throws IOException, TimeoutException {
        System.out.println("Carrier");

        Channel channel = createChannel(HOST_NAME, SPACE_AGENCY_EXCHANGE_NAME, DIRECT_EXCHANGE_TYPE);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        List<ServiceType> serviceTypes = chooseProvidedServices(bufferedReader);
        String queueName = channel.queueDeclare().getQueue();
        for (ServiceType type : serviceTypes) {
            channel.queueBind(queueName, SPACE_AGENCY_EXCHANGE_NAME, type.toString());
        }
        System.out.println("created queue: " + queueName);

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, StandardCharsets.UTF_8);
                System.out.println("Received: " + message);
            }
        };

        System.out.println("Waiting for messages...");
        channel.basicConsume(queueName, true, consumer);
    }
}
