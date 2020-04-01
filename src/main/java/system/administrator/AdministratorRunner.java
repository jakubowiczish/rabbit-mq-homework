package system.administrator;

import lombok.SneakyThrows;

import static system.util.Utils.getFirstReadLine;

public class AdministratorRunner {

    public static void main(String[] args) {
        String name = chooseName();
        Administrator administrator = new Administrator(name);
        administrator.start();
    }

    @SneakyThrows
    private static String chooseName() {
        System.out.println("Choose name as an administrator:");
        return getFirstReadLine();
    }
}
