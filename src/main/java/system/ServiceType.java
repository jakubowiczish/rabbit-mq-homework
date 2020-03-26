package system;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

@AllArgsConstructor
public enum ServiceType {

    PEOPLES_TRANSPORT("people"),
    CARGO_TRANSPORT("cargo"),
    PLACEMENT_ON_ORBIT("orbit"),
    EXIT("exit");

    private String name;

    @Override
    public String toString() {
        return name;
    }

    public static String getAllAvailableTypesAsString() {
        return Arrays.stream(values())
                .map(type -> "[" + type.name + "]")
                .collect(joining());
    }

    public static ServiceType fromString(String text) {
        for (ServiceType serviceType : values()) {
            if (serviceType.name.equalsIgnoreCase(text)) {
                return serviceType;
            }
        }

        throw new IllegalArgumentException("No such service type as: " + text);
    }

    @SneakyThrows
    public static ServiceType chooseService(BufferedReader bufferedReader) {
        System.out.println("Choose service type from available: " + getAllAvailableTypesAsString());
        String serviceTypeString = bufferedReader.readLine();

        ServiceType serviceType = null;
        try {
            serviceType = fromString(serviceTypeString);
        } catch (IllegalArgumentException | NullPointerException e) {
            System.out.println("No such type available, try again");
            chooseService(bufferedReader);
        }

        return serviceType;
    }

    @SneakyThrows
    public static List<ServiceType> chooseProvidedServices(BufferedReader bufferedReader) {
        List<ServiceType> list = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            ServiceType serviceType = chooseService(bufferedReader);
            list.add(serviceType);
        }
        return list;
    }
}
