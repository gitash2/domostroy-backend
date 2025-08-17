package domostroy.core.adapters.adaptersOutput.users.projections;

import domostroy.users.domain.Role;
import domostroy.core.adapters.adaptersOutput.offers.projections.OfferProjection;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Set;


@Getter
@Setter
public class User implements UserDetails {
    private Long id;

    private String email;

    private String firstName;

    private String lastName;

    private String password;

    private String phoneNumber;

    private LocalDate createdAt;

    private Boolean isBanned;

    private Boolean notificationsEnabled;

    private Role role;

    private Set<OfferProjection> favourites;
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.getRole().toString()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
