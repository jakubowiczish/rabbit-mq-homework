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
public enum AdminServiceType {

    CARRIERS_AND_AGENCIES("AGENCY.CARRIER"),
    AGENCIES("AGENCY.#"),
    CARRIERS("#.CARRIER");

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
    public static AdminServiceType fromString(String serviceType) {
        int serviceNumber;
        try {
            serviceNumber = Integer.parseInt(serviceType);
        } catch (NumberFormatException e) {
            printlnColoured("Bad number, you'll be sending message to both agencies and carriers", YELLOW_BOLD_BRIGHT);
            return CARRIERS_AND_AGENCIES;
        }

        switch (serviceNumber) {
            case 1:
                printlnColoured("You chose to send a message to all agencies", MAGENTA_BOLD_BRIGHT);
                return AGENCIES;
            case 2:
                printlnColoured("You chose to send a message to all carriers", MAGENTA_BOLD_BRIGHT);
                return CARRIERS;
            default:
                printlnColoured("You chose to send a message to all agencies and carriers", MAGENTA_BOLD_BRIGHT);
                return CARRIERS_AND_AGENCIES;
        }
    }
}
