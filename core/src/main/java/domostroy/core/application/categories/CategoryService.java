package domostroy.core.application.categories;

import domostroy.core.adapters.adaptersInput.dto.input.mobile.categories.CategoryDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryDTO getCategories() {
        return new CategoryDTO(categoryRepository.findAll());
    }

}
