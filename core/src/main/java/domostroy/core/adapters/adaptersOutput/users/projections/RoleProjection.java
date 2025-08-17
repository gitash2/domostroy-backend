package domostroy.core.adapters.adaptersOutput.users.projections;

import domostroy.users.domain.RoleValue;
import jakarta.persistence.*;
import lombok.Getter;


@Getter
public class RoleProjection {
    Integer id;

    @Enumerated(value = EnumType.STRING)
    RoleValue role;
}
