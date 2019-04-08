package pl.jarosyjarosy.yougetin.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.jarosyjarosy.yougetin.user.endpoint.message.UserMessage;
import pl.jarosyjarosy.yougetin.user.model.User;

@Component
public class UserMapperService {
    private PasswordService passwordService;

    @Autowired
    public UserMapperService(PasswordService passwordService) {
        this.passwordService = passwordService;
    }

    public User mapUserMessage(UserMessage userMessage) {
        User user = new User();
        user.setId(userMessage.getId());
        user.setPassword(passwordService.getPasswordHash(userMessage.getPassword()));
        user.setEmail(userMessage.getEmail());
        user.setName(userMessage.getName());

        return user;
    }

    public UserMessage mapUser(User user) {
        UserMessage userMessage = new UserMessage();
        userMessage.setId(user.getId());
        userMessage.setEmail(user.getEmail());
        userMessage.setName(user.getName());

        return userMessage;
    }
}