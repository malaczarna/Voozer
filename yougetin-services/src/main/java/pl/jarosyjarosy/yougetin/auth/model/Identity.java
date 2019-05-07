package pl.jarosyjarosy.yougetin.auth.model;

import com.google.common.base.Objects;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.impl.DefaultClaims;
import pl.jarosyjarosy.yougetin.user.model.RoleType;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

public class Identity {
    private static final String USER_ID_KEY = "id";
    private static final String ROLES_KEY = "roles";
    private Claims claims;

    public Identity(Claims claims) {
        this.claims = claims;
    }

    public Identity(HttpServletRequest request) {
        this.claims = (Claims) request.getAttribute("claims");
    }

    public Long getUserId() {
        if (claims.containsKey(USER_ID_KEY)) {
            return Long.parseLong(claims.get(USER_ID_KEY).toString());
        }

        throw new EmptyIdentityException("User id not found");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Identity identity = (Identity) o;
        return Objects.equal(claims, identity.claims);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(claims);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("claims", claims)
                .toString();
    }

    public List<RoleType> getRoles() {
        if (claims.containsKey(ROLES_KEY)) {
            return ((List<String>)claims.get(ROLES_KEY)).stream().map(RoleType::valueOf).collect(Collectors.toList());
        }

        throw new EmptyIdentityException("User roles not found");
    }

    public Claims getClaims() {
        return this.claims;
    }

    public static Identity build(Long userId) {
        Claims claims = new DefaultClaims();
        claims.put(USER_ID_KEY, userId);

        return new Identity(claims);
    }
}