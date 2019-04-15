package pl.jarosyjarosy.yougetin.user.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.jarosyjarosy.yougetin.auth.model.Identity;
import pl.jarosyjarosy.yougetin.rest.RecordNotFoundException;
import pl.jarosyjarosy.yougetin.user.model.User;
import pl.jarosyjarosy.yougetin.user.repository.UserRepository;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Date;

@Component
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User get(Long id) {
        LOGGER.info("get user {}", id);

        if(userRepository.findById(id).isPresent()) {
            return userRepository.findById(id).get();
        } else {
            throw new RecordNotFoundException();
        }
    }

    @Transactional
    public User validateAndCreate(User user) {
        LOGGER.info("validateAndCreate user {}", user);

        user.setCreateDate(Date.from(Instant.now()));
        user.setActive(true);
        user.setBlocked(false);

        return userRepository.save(user);
    }

}
