package domostroy.core.application.misc.filter;

public record SearchOperationDTO(
        String alias,
        SearchOperation value
) {
    public SearchOperationDTO(SearchOperation searchOperation) {
        this(searchOperation.getAlias(), searchOperation);
    }
}
