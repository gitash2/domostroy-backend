package domostroy.core.adapters.adaptersOutput.users.projections;

import domostroy.aggregates.users.domain.RoleValue;
import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class RoleProjection {
    @Id
    @GeneratedValue
    Integer id;

    @Enumerated(value = EnumType.STRING)
    RoleValue role;
}
