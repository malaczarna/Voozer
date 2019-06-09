package pl.jarosyjarosy.yougetin.user.endpoint.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import pl.jarosyjarosy.yougetin.user.model.Profile;

import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown=true)
public class UserMessage {

    private Long id;
    private String name;
    private String email;
    private String password;
    private Date createDate;

    private Double lat;
    private Double lng;

    private Profile currentProfile;

    private List<RoleMessage> roles;

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

    public List<RoleMessage> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleMessage> roles) {
        this.roles = roles;
    }
}

