package pl.jarosyjarosy.yougetin.fcm.service;

import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.MulticastMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.jarosyjarosy.yougetin.fcm.model.UserRegistrationToken;
import pl.jarosyjarosy.yougetin.fcm.repository.UserRegistrationTokenRepository;
import pl.jarosyjarosy.yougetin.notification.model.Notification;
import pl.jarosyjarosy.yougetin.trip.service.TripService;

import java.util.List;

@Component
public class FCMService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TripService.class);

    private UserRegistrationTokenRepository userRegistrationTokenRepository;

    @Autowired
    FCMService(UserRegistrationTokenRepository userRegistrationTokenRepository) {
        this.userRegistrationTokenRepository = userRegistrationTokenRepository;
    }

    public UserRegistrationToken saveRegistrationToken(UserRegistrationToken userRegistrationToken) {
         return this.userRegistrationTokenRepository.save(userRegistrationToken);
    }

    public void sendPushNotificationToDriver(Notification notification) throws FirebaseMessagingException {
        List<String> registrationTokens = userRegistrationTokenRepository.findTokensByUserId(notification.getDriverId());

        if(registrationTokens.size() > 0) {
            MulticastMessage message = MulticastMessage.builder().setNotification(
                    new com.google.firebase.messaging.Notification(
                            "Ktoś chcę się z tobą zabrać!",
                            "Użytkownik pyta się czy go podwieziesz. Kliknij aby zobaczyć punkt odbioru"))
                    .putData("passengerId", notification.getPassengerId().toString())
                    .putData("meetingLat", notification.getMeetingLat().toString())
                    .putData("meetingLng", notification.getMeetingLng().toString())
                    .addAllTokens(registrationTokens)
                    .build();
            BatchResponse response = FirebaseMessaging.getInstance().sendMulticast(message);
            LOGGER.info(response.getSuccessCount() + " messages were sent successfully");
        }
    }

}