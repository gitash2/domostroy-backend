package domostroy.core.adapters.adaptersInput.dto.input.mobile.categories;

import domostroy.core.adapters.adaptersOutput.categories.projections.CategoryProjection;

import java.util.List;

public record CategoryDTO(
        List<CategoryProjection> categories
) {
}
