package domostroy.core.adapters.adaptersInput.dto.input.misc;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

public record PaginationAndSortingInput(
        Pagination pagination,
        List<Sorting> sorting
) {
    public PageRequest toPageRequest() {
        Sort sort = getSort();
        if (valid()) {
            return PageRequest.of(
                    pagination.page(),
                    pagination.size(),
                    sort
            );
        }
        throw new IllegalStateException("Invalid page request");
    }

    private Boolean valid() {
        if (!pagination.isValid()) {
            throw new IllegalStateException("Pagination is not valid");
        }

        sorting.forEach(Sorting::validateRequest);
        return true;
    }

    private Sort getSort() {
        if (sorting() == null || sorting().isEmpty()) {
            return Sort.unsorted();
        }
        return Sort.by(
                sorting().stream()
                        .map(Sorting::validateRequest)
                        .map(Sorting::toOrder)
                        .toList()
        );
    }
}
