package system;

import static system.Utils.getFirstReadLine;

public class CarrierRunner {

    public static void main(String[] args) {
        String name = chooseName();
        Carrier carrier = new Carrier(name);
        carrier.start();
    }

    private static String chooseName() {
        System.out.println("Choose name for your carrier: ");
        return getFirstReadLine();
    }
}
