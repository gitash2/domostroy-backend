package domostroy.core.adapters.adaptersOutput.categories.projections;

import domostroy.aggregates.category.Category;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "categories")
@NoArgsConstructor
@AllArgsConstructor
public class CategoryProjection {
    @Id
    private Integer id;

    private String name;


    public CategoryProjection(Category aggregate) {
        this.id = aggregate.id();
        this.name = aggregate.name();
    }

    public Category toAggregate() {
        return new Category(id, name);
    }
}
