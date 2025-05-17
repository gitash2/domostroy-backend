package domostroy.core.adapters.adaptersOutput.categories.dao;

import domostroy.aggregates.category.Category;
import domostroy.core.adapters.adaptersOutput.categories.projections.CategoryProjection;
import domostroy.core.application.categories.CategoryRepository;
import domostroy.core.exceptions.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CategoryJPARepository implements CategoryRepository {
    private final CategoryDAO categoryDAO;


    @Override
    public Category save(Category aggregate) {
        categoryDAO.save(new CategoryProjection(aggregate));
        return aggregate;
    }

    @Override
    public Category findByName(String name) {
        return categoryDAO.findByName(name)
                .orElseThrow(() -> new ObjectNotFoundException("Category is not found : " + name));
    }

    @Override
    public CategoryProjection findById(Integer id) {
        return categoryDAO
                .findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Category is not found : " + id));
    }

    @Override
    public List<CategoryProjection> findAll() {
        return categoryDAO.findAll();
    }

    @Override
    public Long count() {
        return categoryDAO.count();
    }

    @Override
    public List<CategoryProjection> saveAll(List<CategoryProjection> categories) {
        return categoryDAO.saveAll(categories);
    }
}
