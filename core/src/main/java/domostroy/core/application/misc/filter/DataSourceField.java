package domostroy.core.application.misc.filter;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;
import java.util.stream.Collectors;

public record DataSourceField(
        DataSourceFieldKind kind,
        List<SearchOperationDTO> operations,
        @JsonIgnore String dbField
) {

    public boolean hasOperation(String operation) {
        return operations.stream()
                .map(SearchOperationDTO::alias)
                .collect(Collectors.toSet())
                .contains(operation);
    }
}
