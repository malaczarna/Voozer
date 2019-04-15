package pl.jarosyjarosy.yougetin.user.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.jarosyjarosy.yougetin.auth.model.Identity;
import pl.jarosyjarosy.yougetin.user.endpoint.message.UserMessage;
import pl.jarosyjarosy.yougetin.user.model.User;
import pl.jarosyjarosy.yougetin.user.service.UserMapperService;
import pl.jarosyjarosy.yougetin.user.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final UserMapperService userMapperService;

    @Autowired
    public UserController(UserService userService,
                          UserMapperService userMapperService) {
        this.userService = userService;
        this.userMapperService = userMapperService;
    }

    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.GET,
            produces = "application/json"
    )
    public UserMessage get(@PathVariable Long id, HttpServletRequest request) {

        return userMapperService.mapUser(userService.get(id));
    }

    @RequestMapping(
            value = "/register-command",
            method = RequestMethod.POST,
            consumes = "application/json",
            produces = "application/json"
    )
    public UserMessage save(@RequestBody UserMessage user) throws InvalidKeySpecException, NoSuchAlgorithmException {

        User newUser = userService.validateAndCreate(
                userMapperService.mapUserMessage(user)
        );

        return userMapperService.mapUser(newUser);
    }

}
