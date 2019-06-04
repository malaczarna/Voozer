package pl.jarosyjarosy.yougetin.user.model;

public enum Profile {

    PASSENGER("Pasażer"),
    DRIVER("Kierowca");

    private String title;

    Profile(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
