package pl.jarosyjarosy.yougetin.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.jarosyjarosy.yougetin.rest.RecordNotFoundException;
import pl.jarosyjarosy.yougetin.user.model.Role;
import pl.jarosyjarosy.yougetin.user.model.User;
import pl.jarosyjarosy.yougetin.user.repository.RoleRepository;
import pl.jarosyjarosy.yougetin.user.repository.UserRepository;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Component
public class UserService {

    private UserRepository userRepository;
    private UserValidationService userValidationService;
    private RoleRepository roleRepository;

    @Autowired
    public UserService(UserRepository userRepository,
                       UserValidationService userValidationService,
                       RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.userValidationService = userValidationService;
        this.roleRepository = roleRepository;
    }

    public User get(Long id) {

        if(userRepository.findById(id).isPresent()) {
            return userRepository.findById(id).get();
        } else {
            throw new RecordNotFoundException();
        }
    }

    @Transactional
    public User validateAndCreate(User user, List<Role> roles) {

        userValidationService.assureUserWithUniqueEmail(user);
        //userValidationService.assureAtLeastOneRoleIsDefined(roles);
        //userValidationService.assureRolesAreNotDoubled(roles);

        assureDefaultFieldsAreNotEmpty(user, roles);
        user.setCreateDate(Date.from(Instant.now()));
        final User newUser = userRepository.save(user);

        roles.forEach(role -> {
            role.setUserId(newUser.getId());
            roleRepository.save(role);
        });

        return newUser;
    }

    public List<Role> getRoles(Long userId) {
        return roleRepository.findByUserId(userId);
    }

    private void assureDefaultFieldsAreNotEmpty(User user, List<Role> roles) {
        if (user.getActive() == null) {
            user.setActive(true);
        }

        if (user.getBlocked() == null) {
            user.setBlocked(false);
        }

        for (Role role : roles) {
            if (role.getActive() == null) {
                role.setActive(true);
            }
        }

        if (user.getBlocked()) {
            user.setActive(false);

            for (Role role : roles) {
                role.setActive(false);
            }
        }
    }

}
