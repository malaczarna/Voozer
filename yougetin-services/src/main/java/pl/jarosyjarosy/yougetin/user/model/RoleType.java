package pl.jarosyjarosy.yougetin.user.model;

import java.util.EnumSet;

public enum RoleType {
    ADMIN("Administrator"),
    USER("Użytkownik");

    private String title;

    RoleType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public static RoleType getByRole(String title) {
        for (RoleType s : EnumSet.allOf(RoleType.class)) {
            if (s.getTitle().equals(title)) {
                return s;
            }
        }

        return null;
    }
}
