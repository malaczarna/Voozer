package pl.jarosyjarosy.yougetin.fcm.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.jarosyjarosy.yougetin.auth.model.Identity;
import pl.jarosyjarosy.yougetin.fcm.endpoint.model.UserRegistrationTokenMessage;
import pl.jarosyjarosy.yougetin.fcm.model.UserRegistrationToken;
import pl.jarosyjarosy.yougetin.fcm.service.FCMService;
import pl.jarosyjarosy.yougetin.fcm.service.UserRegistrationTokenMapperService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/fcm")
public class UserRegistrationTokenController {

    private FCMService fcmService;
    private UserRegistrationTokenMapperService userRegistrationTokenMapperService;

    @Autowired
    public UserRegistrationTokenController(FCMService fcmService,
                          UserRegistrationTokenMapperService userRegistrationTokenMapperService) {
        this.fcmService = fcmService;
        this.userRegistrationTokenMapperService = userRegistrationTokenMapperService;
    }

    @RequestMapping(
            method = RequestMethod.POST,
            produces = "application/json"
    )
    public UserRegistrationTokenMessage save(@RequestBody String token, HttpServletRequest request) {

        UserRegistrationToken userRegistrationToken = new UserRegistrationToken();
        userRegistrationToken.setUserId(new Identity(request).getUserId());
        userRegistrationToken.setRegistrationToken(token);

        return userRegistrationTokenMapperService.mapUserRegistrationToken(fcmService.saveRegistrationToken(userRegistrationToken));
    }
}
