package system;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Utils {

    private static final String HOST_NAME = "localhost";

    public static Channel createDefaultChannel() {
        return createChannel();
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
