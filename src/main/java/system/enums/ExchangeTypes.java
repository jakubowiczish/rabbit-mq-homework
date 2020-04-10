package system.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExchangeTypes {

    SPACE_AGENCY_EXCHANGE("SPACE_AGENCY_EXCHANGE"),
    ADMIN_EXCHANGE("ADMIN_EXCHANGE");

    private final String name;
}
