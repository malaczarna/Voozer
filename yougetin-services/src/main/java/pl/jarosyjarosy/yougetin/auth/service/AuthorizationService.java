package pl.jarosyjarosy.yougetin.auth.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import pl.jarosyjarosy.yougetin.auth.model.Identity;
import pl.jarosyjarosy.yougetin.user.model.RoleType;

@Component
public class AuthorizationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizationService.class);

    public void checkUserIsAdmin(Identity identity) {
        long count = identity.getRoles().stream().filter(role -> role.equals(RoleType.ADMIN)).count();

        if (count == 0) {
            LOGGER.warn("checkUserIsAdmin identity {}", identity.getUserId());
            throw new AuthorizationException("User is not an Admin");
        }
    }
}
