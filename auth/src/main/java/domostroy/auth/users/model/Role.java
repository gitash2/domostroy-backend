package domostroy.auth.users.model;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "roles")
@Getter
public class Role {
    @Id
    @GeneratedValue(generator = "roles_seq")
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false)
    private RoleValue role;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "role")
    private Collection<User> users = new ArrayList<>();
}
