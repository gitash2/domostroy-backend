package domostroy.auth.users.model;

import lombok.Getter;

@Getter
public enum RoleValue {
    ADMIN("Администратор"),
    USER("Пользователь");

    private final String ruValue;

    RoleValue(String ruValue) {
        this.ruValue = ruValue;
    }
}

