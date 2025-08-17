package domostroy.users.domain;


public final class RoleModel {

    private RoleModel() {
    }

    public static final class Allowed {
        public static final String ADMIN = "hasAuthority('" + RoleValue.C_ADMIN + "')";
        public static final String USER = "hasAuthority('" + RoleValue.C_USER + "')";

        public static final String ADMIN_OR_USER = "hasAuthority('" + RoleValue.C_USER + "or" + RoleValue.C_ADMIN + "')";

    }

    public static final class Forbidden {
        public static final String USER = "!hasAuthority('" + RoleValue.C_USER + "')";
    }
}

