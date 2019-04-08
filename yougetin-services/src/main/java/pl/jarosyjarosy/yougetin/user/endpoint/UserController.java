package pl.jarosyjarosy.yougetin.user.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.jarosyjarosy.yougetin.user.endpoint.message.UserMessage;
import pl.jarosyjarosy.yougetin.user.service.UserMapperService;
import pl.jarosyjarosy.yougetin.user.service.UserService;

import javax.servlet.http.HttpServletRequest;

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
}
