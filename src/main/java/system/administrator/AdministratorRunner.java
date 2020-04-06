package system.administrator;

import static system.util.Utils.chooseName;

public class AdministratorRunner {

    public static void main(String[] args) {
        String name = chooseName("administrator");
        Administrator administrator = new Administrator(name);
        administrator.start();
    }
}
