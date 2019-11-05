package pl.jarosyjarosy.yougetin.fcm.service;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import pl.jarosyjarosy.yougetin.fcm.endpoint.model.UserRegistrationTokenMessage;
import pl.jarosyjarosy.yougetin.fcm.model.UserRegistrationToken;

@Component
public class UserRegistrationTokenMapperService {

    public UserRegistrationTokenMessage mapUserRegistrationToken(UserRegistrationToken userRegistrationToken) {
        UserRegistrationTokenMessage userRegistrationTokenMessage = new UserRegistrationTokenMessage();
        BeanUtils.copyProperties(userRegistrationToken, userRegistrationTokenMessage);

        return userRegistrationTokenMessage;
    }

    public UserRegistrationToken mapUserRegistrationTokenMessage(UserRegistrationTokenMessage userRegistrationTokenMessage) {
        UserRegistrationToken userRegistrationToken = new UserRegistrationToken();
        BeanUtils.copyProperties(userRegistrationTokenMessage, userRegistrationToken);

        return userRegistrationToken;
    }
}
