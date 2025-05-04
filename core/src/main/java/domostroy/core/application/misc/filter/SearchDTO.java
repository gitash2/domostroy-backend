package domostroy.core.application.misc.filter;

import domostroy.core.adapters.adaptersInput.dto.input.misc.PaginationAndSortingInput;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

public record SearchDTO(
        PaginationAndSortingInput pas,
        List<SearchCriteria> searchCriteriaList
) {
    public SearchDTO() {
        this(null, new ArrayList<>());
    }

    public Pageable toRequest() {
        if (pas != null) {
            return pas.toPageRequest();
        }
        return Pageable.unpaged();
    }

    public void validate() {
        if (searchCriteriaList.isEmpty()) {
            throw new IllegalStateException("No search criteria found");
        }

        Integer smallestSearchValue = searchCriteriaList.stream()
                .filter(criteria -> !criteria.isNullable())
                .map(criteria -> criteria.value().toString().length())
                .min(Integer::compare)
                .orElse(null);

        if (smallestSearchValue != null && smallestSearchValue < 2) {
            throw new IllegalStateException("Smallest search value must be less than 2");
        }
    }
}
