package domostroy.core.adapters.adaptersInput.dto.input.misc;

import org.springframework.data.domain.Sort;

public record Sorting(
        String property,
        Sort.Direction direction
) {
    public Sorting() {
        this("", Sort.Direction.ASC);
    }

    public Sorting validateRequest() {
        if (property == null) {
            throw new IllegalStateException("property is null");
        }
        return this;
    }

    public Sort.Order toOrder() {
        return new Sort.Order(direction, property);
    }
}
