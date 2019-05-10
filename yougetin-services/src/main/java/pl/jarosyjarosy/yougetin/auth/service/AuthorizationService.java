package pl.jarosyjarosy.yougetin.auth.service;

import org.springframework.stereotype.Component;
import pl.jarosyjarosy.yougetin.auth.model.Identity;
import pl.jarosyjarosy.yougetin.user.model.RoleType;

@Component
public class AuthorizationService {

    public void checkUserIsAdmin(Identity identity) {
        long count = identity.getRoles().stream().filter(role -> role.equals(RoleType.ADMIN)).count();

        if (count == 0) {
            throw new AuthorizationException("User is not an Admin");
        }
    }
}
