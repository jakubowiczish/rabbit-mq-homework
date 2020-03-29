package system;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Consumer;
import lombok.SneakyThrows;

public class ChannelUtil {

    @SneakyThrows
    public static void consume(Channel channel, String name, Consumer consumer) {
        channel.basicConsume(name, false, consumer);
    }

    public static void stop(String reason) {
        System.out.println(reason);
        System.exit(0);
    }
}
