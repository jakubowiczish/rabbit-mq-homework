package system;

import lombok.SneakyThrows;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static system.Utils.getFirstReadLine;

public class SpaceAgencyRunner {

    public static void main(String[] args) throws IOException, TimeoutException {
        String name = chooseName();
        SpaceAgency spaceAgency = new SpaceAgency(name);
        spaceAgency.start();
    }

    @SneakyThrows
    private static String chooseName() {
        System.out.println("Choose name for your space agency:");
        return getFirstReadLine();
    }
}
