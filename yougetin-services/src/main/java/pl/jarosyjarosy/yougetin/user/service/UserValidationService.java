package pl.jarosyjarosy.yougetin.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.jarosyjarosy.yougetin.user.model.Role;
import pl.jarosyjarosy.yougetin.user.model.User;
import pl.jarosyjarosy.yougetin.user.repository.UserRepository;

import javax.validation.ValidationException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class UserValidationService {

    private UserRepository userRepository;

    @Autowired
    UserValidationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void assureUserWithUniqueEmail(User user) {
        User existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser != null && !Objects.equals(existingUser.getId(), user.getId())) {
            throw new ValidationException("Użytkownik z takim emailem już istnieje");
        }
    }

    public void assureAtLeastOneRoleIsDefined(List<Role> roles) {
        if (roles == null || roles.size() == 0) {
            throw new ValidationException("Zdefiniuj przynajmniej jedną rolę dla użytkownika");
        }
    }

    public void assureRolesAreNotDoubled(List<Role> roles) {
        if (roles != null && roles.size() > 0) {
            if (roles.size() != roles.stream().map(role -> role.getType().getTitle()).collect(Collectors.toSet()).size()) {
                throw new ValidationException("Role użytkownika nie mogą sie powielać");

            }
        }
    }
}
