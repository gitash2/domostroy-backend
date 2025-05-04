package domostroy.core.adapters.adaptersInput.dto.input.misc;

import org.springframework.data.domain.Page;

public record PaginationOutput(
        long totalElements,
        int totalPages,
        boolean hasNext,
        boolean hasPrevious,
        boolean hasContent
) {
    public static PaginationOutput fromPage(Page<?> page) {
        return new PaginationOutput(
                page.getTotalElements(),
                page.getTotalPages(),
                page.hasNext(),
                page.hasPrevious(),
                page.hasContent()
        );
    }
}
