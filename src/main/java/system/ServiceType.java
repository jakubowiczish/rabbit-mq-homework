package system;

import lombok.AllArgsConstructor;

import java.util.stream.IntStream;

import static java.util.stream.Collectors.joining;

@AllArgsConstructor
public enum ServiceType {

    PEOPLES_TRANSPORT("Transport of people"),
    CARGO_TRANSPORT("Transport of cargo"),
    PLACEMENT_ON_ORBIT("Transport to an orbit");

    private String name;

    @Override
    public String toString() {
        return name;
    }

    public static String getAllAvailableTypesAsString() {
        return IntStream.range(0, values().length)
                .mapToObj(i -> "\n" + i + " [" + values()[i] + "]")
                .collect(joining());
    }

    public static void printAllAvailableTypesOfServices(String prefix) {
        System.out.println(prefix);
        System.out.println(getAllAvailableTypesAsString());
        System.out.println("Type \"exit\" to exit");
    }

    public static ServiceType fromString(String serviceType) {
        int serviceNumber = Integer.parseInt(serviceType);

        switch (serviceNumber) {
            case 2:
                return CARGO_TRANSPORT;
            case 3:
                return PLACEMENT_ON_ORBIT;
            default:
                return PEOPLES_TRANSPORT;
        }
    }

}
