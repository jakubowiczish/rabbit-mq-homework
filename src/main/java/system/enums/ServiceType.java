package system.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;

import java.util.stream.IntStream;

import static java.util.stream.Collectors.joining;
import static system.util.ColouredPrinter.printlnColoured;
import static system.util.ConsoleColor.*;

@Getter
@AllArgsConstructor
public enum ServiceType {

    PEOPLES_TRANSPORT("transport of people"),
    CARGO_TRANSPORT("transport of cargo"),
    PLACEMENT_ON_ORBIT("transport to an orbit");

    private String name;

    public static String getAllAvailableTypesAsString() {
        return IntStream.range(0, values().length)
                .mapToObj(i -> i + " [" + values()[i] + "]")
                .collect(joining(", "));
    }

    public static void printAllAvailableTypesOfServices(String prefix) {
        String message = prefix + getAllAvailableTypesAsString() + "\n" + "Type \"exit\" to exit";
        printlnColoured(message, GREEN_BOLD_BRIGHT);
    }

    @SneakyThrows
    public static ServiceType fromString(String serviceType) {
        int serviceNumber;
        try {
            serviceNumber = Integer.parseInt(serviceType);
        } catch (NumberFormatException e) {
            printlnColoured("Bad number, you'll be transporting cargo", YELLOW_BOLD_BRIGHT);
            return CARGO_TRANSPORT;
        }

        switch (serviceNumber) {
            case 1:
                printlnColoured("You chose " + CARGO_TRANSPORT.getName(), MAGENTA_BOLD_BRIGHT);
                return CARGO_TRANSPORT;
            case 2:
                printlnColoured("You chose " + PLACEMENT_ON_ORBIT.getName(), MAGENTA_BOLD_BRIGHT);
                return PLACEMENT_ON_ORBIT;
            default:
                printlnColoured("You chose " + PEOPLES_TRANSPORT.getName(), MAGENTA_BOLD_BRIGHT);
                return PEOPLES_TRANSPORT;
        }
    }
}
