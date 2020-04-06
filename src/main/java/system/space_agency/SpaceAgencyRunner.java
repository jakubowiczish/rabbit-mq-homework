package system.space_agency;

import static system.util.Utils.chooseName;

public class SpaceAgencyRunner {

    public static void main(String[] args) {
        String name = chooseName("space agency");
        SpaceAgency spaceAgency = new SpaceAgency(name);
        spaceAgency.start();
    }
}
