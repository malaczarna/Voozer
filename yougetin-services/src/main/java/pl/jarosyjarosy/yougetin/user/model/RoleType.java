package pl.jarosyjarosy.yougetin.user.model;

import java.util.EnumSet;

public enum RoleType {
    PASSENGER("Pasażer"),
    DRIVER("Kierowca");

    private String title;

    RoleType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

}
