package domostroy.aggregates.currency;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Currency {
    RUB("Рубль");

    private final String name;
}
