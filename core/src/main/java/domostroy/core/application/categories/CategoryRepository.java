package domostroy.core.application.categories;

import domostroy.aggregates.category.Category;
import domostroy.core.adapters.adaptersOutput.categories.projections.CategoryProjection;

import java.util.List;

public interface CategoryRepository {
    Category save(Category aggregate);
    Category findByName(String name);
    Category findById(Integer id);
    List<CategoryProjection> findAll();
    Long count();
    List<CategoryProjection> saveAll(List<CategoryProjection> categories);
}
