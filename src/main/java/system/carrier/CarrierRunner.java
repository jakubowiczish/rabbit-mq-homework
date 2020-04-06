package system.carrier;

import static system.util.Utils.chooseName;

public class CarrierRunner {

    public static void main(String[] args) {
        String name = chooseName("carrier");
        Carrier carrier = new Carrier(name);
        carrier.start();
    }
}
