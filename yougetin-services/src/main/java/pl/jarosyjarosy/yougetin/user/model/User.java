package pl.jarosyjarosy.yougetin.user.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotBlank(message = "Uzupełnij pole Imię")
    private String name;
    @NotBlank(message = "Uzupełnij pole Email")
    @Email(message = "Nieprawidłowy format pola Email")
    private String email;
    private String password;

    private Date createDate;

    private Date lastActivity;
    private Boolean active;
    private Long destinationId;
    private Boolean blocked;

    private Long travelingUserId;

    private Double lat;
    private Double lng;
    @Enumerated(EnumType.STRING)
    private Profile currentProfile;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getLastActivity() {
        return lastActivity;
    }

    public void setLastActivity(Date lastActivity) {
        this.lastActivity = lastActivity;
    }

    public Boolean getActive() {
        return active;
    }

    public Long getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(Long destinationId) {
        this.destinationId = destinationId;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getBlocked() {
        return blocked;
    }

    public void setBlocked(Boolean blocked) {
        this.blocked = blocked;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Profile getCurrentProfile() {
        return currentProfile;
    }

    public void setCurrentProfile(Profile currentProfile) {
        this.currentProfile = currentProfile;
    }

    public Long getTravelingUserId() {
        return travelingUserId;
    }

    public void setTravelingUserId(Long travelingUserId) {
        this.travelingUserId = travelingUserId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equal(id, user.id) &&
                Objects.equal(name, user.name) &&
                Objects.equal(email, user.email) &&
                Objects.equal(password, user.password) &&
                Objects.equal(createDate, user.createDate) &&
                Objects.equal(lastActivity, user.lastActivity) &&
                Objects.equal(active, user.active) &&
                Objects.equal(destinationId, user.destinationId) &&
                Objects.equal(blocked, user.blocked) &&
                Objects.equal(lat, user.lat) &&
                Objects.equal(lng, user.lng) &&
                currentProfile == user.currentProfile;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, name, email, password, createDate, lastActivity, active, destinationId, blocked, lat, lng, currentProfile);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("name", name)
                .add("email", email)
                .add("password", password)
                .add("createDate", createDate)
                .add("lastActivity", lastActivity)
                .add("active", active)
                .add("destinationId", destinationId)
                .add("blocked", blocked)
                .add("lat", lat)
                .add("lng", lng)
                .add("currentProfile", currentProfile)
                .toString();
    }
}
