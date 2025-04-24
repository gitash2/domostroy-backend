package domostroy.core.adapters.adaptersOutput.categories.projections;

import domostroy.aggregates.category.Category;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "categories")
@NoArgsConstructor
public class CategoryProjection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
