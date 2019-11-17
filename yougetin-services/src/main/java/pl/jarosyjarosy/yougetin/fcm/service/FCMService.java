package pl.jarosyjarosy.yougetin.fcm.service;

import com.google.firebase.messaging.*;
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

    public void sendPushNotificationToDriver(Notification notification, String passengerName) throws FirebaseMessagingException {
        List<String> registrationTokens = userRegistrationTokenRepository.findTokensByUserId(notification.getDriverId());

        if(registrationTokens.size() > 0) {
            Message message = Message.builder()
                    .setAndroidConfig(
                            AndroidConfig.builder()
                                    .setNotification(AndroidNotification.builder().setClickAction("SHOW_MESSAGE").build())
                                    .setPriority(AndroidConfig.Priority.HIGH).build())
                    .setNotification(new com.google.firebase.messaging.Notification(
                            "Ktoś chcę się z Tobą zabrać!",
                            passengerName + " pyta się czy go podwieziesz. Kliknij aby zobaczyć punkt odbioru."))
                    .putData("passengerId", notification.getPassengerId().toString())
                    .putData("meetingLat", notification.getMeetingLat().toString())
                    .putData("meetingLng", notification.getMeetingLng().toString())
                    .setToken(registrationTokens.get(0))
                    .build();
            String response = FirebaseMessaging.getInstance().send(message);
            LOGGER.info("Successfully sent message:" + response);
        }
    }

}