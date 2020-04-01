package system.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;

import java.util.stream.IntStream;

import static java.util.stream.Collectors.joining;

@Getter
@AllArgsConstructor
public enum AdminServiceType {

    CARRIERS_AND_AGENCIES("AGENCY.CARRIER"),
    AGENCIES("AGENCY.#"),
    CARRIERS("CARRIER.#");

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
    public static AdminServiceType fromString(String serviceType) {
        int serviceNumber;
        try {
            serviceNumber = Integer.parseInt(serviceType);
        } catch (NumberFormatException e) {
            System.out.println("Bad number, you'll be sending message to both agencies and carriers");
            return CARRIERS_AND_AGENCIES;
        }

        switch (serviceNumber) {
            case 1:
//                System.out.println("You chose " + CARGO_TRANSPORT.toString());
                return AGENCIES;
            case 2:
//                System.out.println("You chose " + PLACEMENT_ON_ORBIT.toString());
                return CARRIERS;
            default:
//                System.out.println("You chose " + PEOPLES_TRANSPORT.toString());
                return CARRIERS_AND_AGENCIES;
        }
    }
}
