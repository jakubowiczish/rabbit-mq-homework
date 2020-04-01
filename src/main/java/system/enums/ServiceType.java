package system.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;

import java.util.stream.IntStream;

import static java.util.stream.Collectors.joining;

@Getter
@AllArgsConstructor
public enum ServiceType {

    PEOPLES_TRANSPORT("transport of people"),
    CARGO_TRANSPORT("transport of cargo"),
    PLACEMENT_ON_ORBIT("transport to an orbit");

    private String name;

    public static String getAllAvailableTypesAsString() {
        return IntStream.range(0, values().length)
                .mapToObj(i -> "\n\t- " + i + " [" + values()[i] + "]")
                .collect(joining());
    }

    public static void printAllAvailableTypesOfServices(String prefix) {
        System.out.print(prefix);
        System.out.println(getAllAvailableTypesAsString());
        System.out.println("Type \"exit\" to exit");
    }

    @SneakyThrows
    public static ServiceType fromString(String serviceType) {
        int serviceNumber;
        try {
            serviceNumber = Integer.parseInt(serviceType);
        } catch (NumberFormatException e) {
            System.out.println("Bad number, you'll be transporting cargo");
            return CARGO_TRANSPORT;
        }

        switch (serviceNumber) {
            case 1:
                System.out.println("You chose " + CARGO_TRANSPORT.toString());
                return CARGO_TRANSPORT;
            case 2:
                System.out.println("You chose " + PLACEMENT_ON_ORBIT.toString());
                return PLACEMENT_ON_ORBIT;
            default:
                System.out.println("You chose " + PEOPLES_TRANSPORT.toString());
                return PEOPLES_TRANSPORT;
        }
    }

}
