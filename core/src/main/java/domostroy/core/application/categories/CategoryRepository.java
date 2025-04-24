package domostroy.core.application.categories;

import domostroy.aggregates.category.Category;

public interface CategoryRepository {
    Category save(Category aggregate);
    Category findByName(String name);
    Category findById(Integer id);
}
