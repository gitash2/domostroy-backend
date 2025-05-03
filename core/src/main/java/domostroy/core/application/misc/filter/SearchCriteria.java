package domostroy.core.application.misc.filter;

import io.swagger.v3.oas.annotations.media.Schema;

public record SearchCriteria(
        String filterKey,
        @Schema(
                description = """
                Операция поиска:
                - `cn` — содержит (CONTAINS)
                - `nc` — не содержит (DOES_NOT_CONTAIN)
                - `eq` — равно (EQUAL)
                - `ne` — не равно (NOT_EQUAL)
                - `bw` — начинается с (BEGINS_WITH)
                - `bn` — не начинается с (DOES_NOT_BEGIN_WITH)
                - `ew` — заканчивается на (ENDS_WITH)
                - `en` — не заканчивается на (DOES_NOT_END_WITH)
                - `nu` — значение null (IS_NULL)
                - `nn` — значение не null (NOT_NULL)
                - `gt` — больше (GREATER_THAN)
                - `ge` — больше или равно (GREATER_THAN_EQUAL)
                - `lt` — меньше (LESS_THAN)
                - `le` — меньше или равно (LESS_THAN_EQUAL)
                """,
                allowableValues = {
                        "cn", "nc", "eq", "ne",
                        "bw", "bn", "ew", "en",
                        "nu", "nn", "gt", "ge", "lt", "le"
                },
                example = "eq"
        )
        String operation,
        Object value
) {
    public SearchCriteria(String filterKey, String operation) {
        this(filterKey, operation, null);
    }

    public void validate(DataSourceField dataSourceField) {
        if (!(dataSourceField != null && dataSourceField.hasOperation(operation))) {
            throw new IllegalArgumentException("Operation is not valid");
        }
    }

    public boolean isNullable() {
        return operation.equals(SearchOperation.NOT_NULL.getAlias())
                || operation.equals(SearchOperation.IS_NULL.getAlias());
    }
}
