package domostroy.core.adapters.adaptersInput.api.mobile;

import domostroy.core.adapters.adaptersInput.dto.input.mobile.categories.CategoryDTO;
import domostroy.core.application.categories.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    @Operation(summary = "Получить список всех категории")
    public ResponseEntity<CategoryDTO> getCategories() {
        return ok(categoryService.getCategories());
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDTO> getCategory(@PathVariable int categoryId) {
        return ok(categoryService.getCategory(categoryId));
    }

}
