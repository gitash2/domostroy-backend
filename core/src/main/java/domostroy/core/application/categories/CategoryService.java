package domostroy.core.application.categories;

import domostroy.core.adapters.adaptersInput.dto.input.mobile.categories.CategoryDTO;
import domostroy.core.adapters.adaptersOutput.categories.projections.CategoryProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryDTO getCategories() {
        return new CategoryDTO(categoryRepository.findAll());
    }

    public CategoryDTO getCategory(int categoryId) {
        CategoryProjection category = categoryRepository.findById(categoryId);
        return new CategoryDTO(Collections.singletonList(category));
    }
}
