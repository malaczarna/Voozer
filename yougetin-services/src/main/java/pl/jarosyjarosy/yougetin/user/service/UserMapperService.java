package pl.jarosyjarosy.yougetin.user.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.jarosyjarosy.yougetin.user.endpoint.message.RoleMessage;
import pl.jarosyjarosy.yougetin.user.endpoint.message.UserMessage;
import pl.jarosyjarosy.yougetin.user.model.Role;
import pl.jarosyjarosy.yougetin.user.model.User;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapperService {
    private PasswordService passwordService;

    @Autowired
    public UserMapperService(PasswordService passwordService) {
        this.passwordService = passwordService;
    }

    public User mapUserMessage(UserMessage userMessage) throws InvalidKeySpecException, NoSuchAlgorithmException {
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
        userMessage.setCurrentProfile(user.getCurrentProfile());
        userMessage.setLat(user.getLat());
        userMessage.setLng(user.getLng());

        return userMessage;
    }

    public RoleMessage mapRole(Role role) {
        RoleMessage roleMessage = new RoleMessage();
        BeanUtils.copyProperties(role, roleMessage);

        return roleMessage;
    }

     public Role mapRoleMessage(RoleMessage roleMessage) {
        Role role = new Role();
        BeanUtils.copyProperties(roleMessage, role);

        return role;
    }

    private List<RoleMessage> mapRoles(List<Role> roles) {
        return roles.stream().map(this::mapRole).collect(Collectors.toList());
    }

    public List<Role> mapRoles(UserMessage user) {
        if (user.getRoles() == null) {
            return Collections.emptyList();
        }
        return user.getRoles().stream().map(this::mapRoleMessage).collect(Collectors.toList());
    }
}