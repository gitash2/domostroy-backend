package domostroy.core.adapters.adaptersOutput.categories.dao;

import domostroy.aggregates.category.Category;
import domostroy.core.adapters.adaptersOutput.categories.projections.CategoryProjection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryDAO extends JpaRepository<CategoryProjection, Integer> {
    Optional<Category> findByName(String name);
}
