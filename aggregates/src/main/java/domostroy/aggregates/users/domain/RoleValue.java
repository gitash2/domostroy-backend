package domostroy.aggregates.users.domain;

import lombok.Getter;

@Getter
public enum RoleValue {
    ADMIN("Администратор"),
    USER("Пользователь");

    private final String ruValue;

    RoleValue(String ruValue) {
        this.ruValue = ruValue;
    }

    public static final String C_ADMIN = "'ADMIN'";
    public static final String C_USER = "'USER'";

}

