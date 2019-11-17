package pl.jarosyjarosy.yougetin.fcm.endpoint.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.base.Objects;

import javax.persistence.*;

@JsonIgnoreProperties(ignoreUnknown=true)
public class UserRegistrationTokenMessage {

    private Long id;
    private Long userId;
    private String registrationToken;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getRegistrationToken() {
        return registrationToken;
    }

    public void setRegistrationToken(String registrationToken) {
        this.registrationToken = registrationToken;
    }

}
