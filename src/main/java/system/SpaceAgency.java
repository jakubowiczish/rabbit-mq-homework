package system;

import com.rabbitmq.client.Channel;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import static system.ChannelUtils.*;
import static system.ServiceType.*;

public class SpaceAgency {

    public static void main(String[] args) throws IOException, TimeoutException {
        System.out.println("Space Agency");

        Channel channel = createChannel(HOST_NAME, SPACE_AGENCY_EXCHANGE_NAME, DIRECT_EXCHANGE_TYPE);

        List<Integer> requestsIds = new ArrayList<>();

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String agencyName = chooseAgencyName(bufferedReader);

        while (true) {
            ServiceType serviceType = chooseService(bufferedReader);
            if (ServiceType.EXIT.equals(serviceType)) {
                break;
            }

            int id = requestsIds.size() + 1;
            requestsIds.add(id);
            String message = "Request from agency: " + agencyName + ". Request id: " + id;
            channel.basicPublish(SPACE_AGENCY_EXCHANGE_NAME,
                    serviceType.toString(),
                    null,
                    message.getBytes(StandardCharsets.UTF_8));
        }
    }

    @SneakyThrows
    private static String chooseAgencyName(BufferedReader bufferedReader) {
        System.out.println("Choose a name for your space agency: ");
        return bufferedReader.readLine();
    }
}
