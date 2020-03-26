package system;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ChannelUtils {

    public static final String SPACE_AGENCY_EXCHANGE_NAME = "SPACE_AGENCY_EXCHANGE_NAME";

    public static final String HOST_NAME = "localhost";

    public static final BuiltinExchangeType DIRECT_EXCHANGE_TYPE = BuiltinExchangeType.DIRECT;

    public static Channel createChannel(String host, String exchangeName, BuiltinExchangeType type) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(exchangeName, type);
        return channel;
    }
}
