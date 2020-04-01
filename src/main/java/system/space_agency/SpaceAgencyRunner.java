package system.space_agency;

import lombok.SneakyThrows;

import static system.util.Utils.getFirstReadLine;

public class SpaceAgencyRunner {

    public static void main(String[] args) {
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
