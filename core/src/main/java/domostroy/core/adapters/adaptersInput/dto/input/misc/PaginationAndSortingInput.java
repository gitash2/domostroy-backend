package domostroy.core.adapters.adaptersInput.dto.input.misc;

import domostroy.core.application.misc.filter.RandomPageRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.Instant;
import java.util.List;

public record PaginationAndSortingInput(
        Pagination pagination,
        List<Sorting> sorting,
        String seed,
        Instant snapshot
) {
    public Pageable toPageRequest() {
        if (!pagination.isValid()) {
            throw new IllegalStateException("Invalid pagination");
        }

        if (seed != null && snapshot != null) {
            return PageRequest.of(
                    pagination.page(),
                    pagination.size(),
                    Sort.unsorted()
            );
        }

        Sort sort = (sorting == null || sorting.isEmpty())
                ? Sort.unsorted()
                : Sort.by(
                sorting.stream()
                        .map(Sorting::validateRequest)
                        .map(Sorting::toOrder)
                        .toList()
        );

        return PageRequest.of(
                pagination.page(),
                pagination.size(),
                sort
        );
    }

    private Boolean valid() {
        if (!pagination.isValid()) {
            throw new IllegalStateException("Pagination is not valid");
        }

        sorting.forEach(Sorting::validateRequest);
        return true;
    }

    private Sort getSort() {
        if ((sorting() == null || sorting().isEmpty())
                && seed != null
                && snapshot != null) {
            return Sort.unsorted();
        }
        assert sorting() != null;
        return Sort.by(
                sorting().stream()
                        .map(Sorting::validateRequest)
                        .map(Sorting::toOrder)
                        .toList()
        );
    }
}
