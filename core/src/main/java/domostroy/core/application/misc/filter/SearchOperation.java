package domostroy.core.application.misc.filter;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.Set;

@Getter
@Schema(description = "Операции поиска с alias (пример: CONTAINS = cn, EQUAL = eq и т.д.)")
public enum SearchOperation {

    @Schema(description = "Содержит (alias = cn)")
    CONTAINS("cn"),

    @Schema(description = "Не содержит (alias = nc)")
    DOES_NOT_CONTAIN("nc"),

    @Schema(description = "Равно (alias = eq)")
    EQUAL("eq"),

    @Schema(description = "Не равно (alias = ne)")
    NOT_EQUAL("ne"),

    @Schema(description = "Начинается с (alias = bw)")
    BEGINS_WITH("bw"),

    @Schema(description = "Не начинается с (alias = bn)")
    DOES_NOT_BEGIN_WITH("bn"),

    @Schema(description = "Заканчивается на (alias = ew)")
    ENDS_WITH("ew"),

    @Schema(description = "Не заканчивается на (alias = en)")
    DOES_NOT_END_WITH("en"),

    @Schema(description = "Пустое значение (alias = nu)")
    IS_NULL("nu"),

    @Schema(description = "Не пустое значение (alias = nn)")
    NOT_NULL("nn"),

    @Schema(description = "Больше (alias = gt)")
    GREATER_THAN("gt"),

    @Schema(description = "Больше или равно (alias = ge)")
    GREATER_THAN_EQUAL("ge"),

    @Schema(description = "Меньше (alias = lt)")
    LESS_THAN("lt"),

    @Schema(description = "Меньше или равно (alias = le)")
    LESS_THAN_EQUAL("le");

    private final String alias;

    SearchOperation(String alias) {
        this.alias = alias;
    }

    public static Set<SearchOperation> none() {
        return Set.of();
    }

    public static Set<SearchOperation> forId() {
        return Set.of(EQUAL, NOT_EQUAL);
    }

    public static Set<SearchOperation> forStrField() {
        return Set.of(
                CONTAINS, DOES_NOT_CONTAIN,
                EQUAL, NOT_EQUAL,
                BEGINS_WITH, DOES_NOT_BEGIN_WITH,
                ENDS_WITH, DOES_NOT_END_WITH,
                IS_NULL, NOT_NULL
        );
    }

    public static Set<SearchOperation> forNumber() {
        return Set.of(
                EQUAL, NOT_EQUAL,
                IS_NULL, NOT_NULL,
                GREATER_THAN, GREATER_THAN_EQUAL,
                LESS_THAN, LESS_THAN_EQUAL
        );
    }

    public static Set<SearchOperation> forBoolean() {
        return Set.of(
                EQUAL, NOT_EQUAL,
                IS_NULL, NOT_NULL
        );
    }

    public static Set<SearchOperation> forDate() {
        return Set.of(
                EQUAL,
                GREATER_THAN, GREATER_THAN_EQUAL,
                LESS_THAN, LESS_THAN_EQUAL
        );
    }
}
