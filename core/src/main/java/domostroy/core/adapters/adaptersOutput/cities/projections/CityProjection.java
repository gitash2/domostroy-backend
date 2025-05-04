package domostroy.core.adapters.adaptersOutput.cities.projections;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "cities")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CityProjection {
    @Id
    private Integer id;

    private String name;
}
