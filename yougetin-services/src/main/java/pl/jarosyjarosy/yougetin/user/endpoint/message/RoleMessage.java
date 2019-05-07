package pl.jarosyjarosy.yougetin.user.endpoint.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import pl.jarosyjarosy.yougetin.user.model.RoleType;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RoleMessage {
    private Long id;
    private Long userId;
    private RoleType type;

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

    public RoleType getType() {
        return type;
    }

    public void setType(RoleType type) {
        this.type = type;
    }
}

