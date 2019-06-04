package pl.jarosyjarosy.yougetin.user.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.jarosyjarosy.yougetin.rest.RecordNotFoundException;
import pl.jarosyjarosy.yougetin.user.endpoint.message.Position;
import pl.jarosyjarosy.yougetin.user.model.Profile;
import pl.jarosyjarosy.yougetin.user.model.Role;
import pl.jarosyjarosy.yougetin.user.model.User;
import pl.jarosyjarosy.yougetin.user.repository.RoleRepository;
import pl.jarosyjarosy.yougetin.user.repository.UserRepository;

import javax.transaction.Transactional;
import java.time.Clock;
import java.util.Date;
import java.util.List;

@Component
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private UserRepository userRepository;
    private UserValidationService userValidationService;
    private RoleRepository roleRepository;
    private Clock clock;

    @Autowired
    public UserService(UserRepository userRepository,
                       UserValidationService userValidationService,
                       RoleRepository roleRepository,
                       Clock clock) {
        this.userRepository = userRepository;
        this.userValidationService = userValidationService;
        this.roleRepository = roleRepository;
        this.clock = clock;
    }

    public User get(Long id) {
        LOGGER.info("LOGGER: get user {}", id);
        if(userRepository.findById(id).isPresent()) {
            return userRepository.findById(id).get();
        } else {
            throw new RecordNotFoundException();
        }
    }

    @Transactional
    public User validateAndCreate(User user, List<Role> roles) {
        LOGGER.info("LOGGER: validate and create user {}", user.getEmail());

        userValidationService.assureUserWithUniqueEmail(user);
        userValidationService.assureAtLeastOneRoleIsDefined(roles);
        userValidationService.assureRolesAreNotDoubled(roles);

        assureDefaultFieldsAreNotEmpty(user);
        user.setCreateDate(Date.from(clock.instant()));
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

    private void assureDefaultFieldsAreNotEmpty(User user) {
        if (user.getActive() == null) {
            user.setActive(true);
        }

        if (user.getCurrentProfile()  == null) {
            user.setCurrentProfile(Profile.PASSENGER);
        }

        if (user.getBlocked() == null) {
            user.setBlocked(false);
        }

        if (user.getBlocked()) {
            user.setActive(false);
        }
    }

    public Position getUserPosition(Long id) {
        LOGGER.info("LOGGER: get user {} position", id);
        User user = get(id);
        Position userPosition = new Position();

        userPosition.setLat(user.getLat());
        userPosition.setLng(user.getLng());

        return userPosition;
    }

    @Transactional
    public User setUserPosition(Long id, Position position) {
        LOGGER.info("LOGGER: set user {} position", id);
        User user = get(id);

        user.setLat(position.getLat());
        user.setLng(position.getLng());

        return userRepository.save(user);
    }

    @Transactional
    public User changeProfile(Long id) {
        LOGGER.info("LOGGER: change user {} profile ", id);
        User user = get(id);
        if (user.getCurrentProfile() != null && user.getCurrentProfile().equals(Profile.PASSENGER)) {
            user.setCurrentProfile(Profile.DRIVER);
        } else {
            user.setCurrentProfile(Profile.PASSENGER);
        }

        return userRepository.save(user);
    }

    public List<User> getUsersWithDifferentProfile(Long id) {
        LOGGER.info("LOGGER: get different then user {} profile ", id);
        User user = get(id);
        if (user.getCurrentProfile() != null && user.getCurrentProfile().equals(Profile.DRIVER)){
            return userRepository.findAllByCurrentProfile(Profile.PASSENGER);
        } else {
            return userRepository.findAllByCurrentProfile(Profile.DRIVER);
        }
    }

    public List<User> getInactiveUsers() {
        return userRepository.findInactive();
    }

    public void setUserAsInactive(User user) {
        user.setActive(false);
        userRepository.save(user);
    }

    public void setLastActivity(Long id) {
        User user = get(id);
        user.setLastActivity(Date.from(clock.instant()));
        user.setActive(true);
        userRepository.save(user);
    }

}
