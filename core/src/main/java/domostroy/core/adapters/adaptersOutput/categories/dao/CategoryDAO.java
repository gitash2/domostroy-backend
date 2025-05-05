package domostroy.core.adapters.adaptersOutput.categories.dao;

import domostroy.aggregates.category.Category;
import domostroy.core.adapters.adaptersOutput.categories.projections.CategoryProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoryDAO extends JpaRepository<CategoryProjection, Integer> {


    Optional<Category> findByName(String name);
}
