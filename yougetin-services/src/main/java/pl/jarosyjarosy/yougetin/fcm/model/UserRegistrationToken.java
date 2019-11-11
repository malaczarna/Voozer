package pl.jarosyjarosy.yougetin.fcm.model;

import com.google.common.base.Objects;

import javax.persistence.*;

@Entity
@Table(name = "user_registration_tokens")
public class UserRegistrationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRegistrationToken that = (UserRegistrationToken) o;
        return Objects.equal(id, that.id) &&
                Objects.equal(userId, that.userId) &&
                Objects.equal(registrationToken, that.registrationToken);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, userId, registrationToken);
    }
}
