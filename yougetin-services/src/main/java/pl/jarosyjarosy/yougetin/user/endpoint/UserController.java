package pl.jarosyjarosy.yougetin.user.endpoint;

import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.jarosyjarosy.yougetin.auth.model.Identity;
import pl.jarosyjarosy.yougetin.destination.model.Destination;
import pl.jarosyjarosy.yougetin.destination.service.DestinationService;
import pl.jarosyjarosy.yougetin.user.endpoint.message.Position;
import pl.jarosyjarosy.yougetin.user.endpoint.message.UserMessage;
import pl.jarosyjarosy.yougetin.user.model.User;
import pl.jarosyjarosy.yougetin.user.service.UserMapperService;
import pl.jarosyjarosy.yougetin.user.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {
    private UserService userService;
    private DestinationService destinationService;
    private UserMapperService userMapperService;

    @Autowired
    public UserController(UserService userService,
                          DestinationService destinationService,
                          UserMapperService userMapperService) {
        this.userService = userService;
        this.destinationService = destinationService;
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
                userMapperService.mapUserMessage(user),
                userMapperService.mapRoles(user)
        );

        return userMapperService.mapUser(newUser);
    }

    @RequestMapping(
            value = "/position",
            method = RequestMethod.GET,
            produces = "application/json"
    )
    public Position getUserPosition(HttpServletRequest request) {

        return userService.getUserPosition(new Identity(request).getUserId());
    }

    @RequestMapping(
            value = "/position",
            method = RequestMethod.POST,
            consumes = "application/json",
            produces = "application/json"
    )
    public UserMessage saveUserPosition(@RequestBody Position position, HttpServletRequest request) {

        return userMapperService.mapUser(userService.setUserPosition(new Identity(request).getUserId(), position));
    }

    @RequestMapping(
            value = "/info",
            method = RequestMethod.GET,
            produces = "application/json"
    )
    public UserMessage getCurrentProfile(HttpServletRequest request) {

        return userMapperService.mapUser(userService.get(new Identity(request).getUserId()));
    }

    @RequestMapping(
            value = "/change-profile-command",
            method = RequestMethod.POST,
            produces = "application/json"
    )
    public UserMessage changeProfile(HttpServletRequest request) {

        return userMapperService.mapUser(userService.changeProfile(new Identity(request).getUserId()));
    }

    @RequestMapping(
            value = "/others",
            method = RequestMethod.GET,
            produces = "application/json"
    )
    public List<UserMessage> getOthersUsers(HttpServletRequest request) {

        return userService.getUsersWithDifferentProfile(new Identity(request).getUserId()).stream()
                .map(userMapperService::mapUser)
                .collect(Collectors.toList());
    }

    @RequestMapping(
            value = "/active/drivers",
            method = RequestMethod.GET,
            produces = "application/json"
    )
    public List<UserMessage> getActiveDrivers(HttpServletRequest request) throws TransformException, FactoryException {

        User user = userService.get(new Identity(request).getUserId());
        Destination destination = destinationService.getByUserId(user.getId()).get(0);

        return userService.getSortedActiveDrivers(destination, user).stream()
                .map(userMapperService::mapUser)
                .collect(Collectors.toList());
    }

    @RequestMapping(
            value = "/stop-destination-command",
            method = RequestMethod.POST,
            produces = "application/json"
    )
    public UserMessage stopDestination(HttpServletRequest request) {

        return userMapperService.mapUser(userService.stopDestination(new Identity(request).getUserId()));
    }


}
