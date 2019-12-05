package pl.jarosyjarosy.yougetin.fcm.service;

import com.google.firebase.messaging.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.jarosyjarosy.yougetin.fcm.model.UserRegistrationToken;
import pl.jarosyjarosy.yougetin.fcm.repository.UserRegistrationTokenRepository;
import pl.jarosyjarosy.yougetin.notification.model.Notification;
import pl.jarosyjarosy.yougetin.notification.model.NotificationType;
import pl.jarosyjarosy.yougetin.trip.service.TripService;

import javax.transaction.Transactional;
import java.util.List;

@Component
public class FCMService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TripService.class);

    private UserRegistrationTokenRepository userRegistrationTokenRepository;

    @Autowired
    FCMService(UserRegistrationTokenRepository userRegistrationTokenRepository) {
        this.userRegistrationTokenRepository = userRegistrationTokenRepository;
    }

    @Transactional
    public UserRegistrationToken saveRegistrationToken(UserRegistrationToken userRegistrationToken) {
        LOGGER.info("Registration token saved for user {}", userRegistrationToken.getUserId());
        return this.userRegistrationTokenRepository.save(userRegistrationToken);
    }

    @Transactional
    public void deleteRegistrationTokensByUser(Long userId) {
        LOGGER.info("Registration tokens deleted for user {}", userId);
        this.userRegistrationTokenRepository.deleteByUserId(userId);
    }

    public void sendPushNotification(Notification notification, Long receiverId, String name, NotificationType type, String title, String body) throws FirebaseMessagingException {
        List<String> registrationTokens = userRegistrationTokenRepository.findTokensByUserId(receiverId);

        if (registrationTokens.size() > 0) {
            Message message = Message.builder()
                    .setAndroidConfig(
                            AndroidConfig.builder()
                                    .setNotification(AndroidNotification.builder().setClickAction("SHOW_MESSAGE").build())
                                    .setPriority(AndroidConfig.Priority.HIGH).build())
                    .setNotification(new com.google.firebase.messaging.Notification(
                            title,
                            name + body))
                    .putData("passengerId", notification.getPassengerId().toString())
                    .putData("driverId", notification.getDriverId().toString())
                    .putData("meetingLat", notification.getMeetingLat().toString())
                    .putData("meetingLng", notification.getMeetingLng().toString())
                    .putData("type", type.toString())
                    .setToken(registrationTokens.get(0))
                    .build();
            String response = FirebaseMessaging.getInstance().send(message);
            LOGGER.info("Successfully sent message:" + response);
        } else {
            LOGGER.info("No registration tokens for user {}", notification.getDriverId());
        }
    }

    public void sendPushNotificationToDriver(Notification notification, String passengerName) throws FirebaseMessagingException {
        sendPushNotification(notification, notification.getDriverId(), passengerName, NotificationType.ASK, "Ktoś chcę się z Tobą zabrać!", " pyta się czy go podwieziesz. Kliknij aby zobaczyć punkt odbioru.");
    }

    public void sendDeclineToPassenger(Notification notification, String driverName) throws FirebaseMessagingException {
        sendPushNotification(notification, notification.getPassengerId(), driverName, NotificationType.DECLINE, "Kierowca odmówił przejazdu.", " niestety nie może Cię podwieźć. Poszukaj innego kierowcy.");
    }

    public void sendAcceptanceToPassenger(Notification notification, String driverName) throws FirebaseMessagingException {
        sendPushNotification(notification, notification.getPassengerId(), driverName, NotificationType.ACCEPT, "Kierowca chce cię zabrać!", " chętnie Cie zabierze. Tylko się nie spóźnij!");
    }

    public void sendStopRequestNotification(Notification notification, String passengerName) throws FirebaseMessagingException {
        sendPushNotification(notification, notification.getDriverId(), passengerName, NotificationType.DECLINE, "Pasażer się rozmyślił", " już nie chce się z Tobą zabrać");
    }

    public void sendPassengerAtMeetingPointNotification(Notification notification, String passengerName) throws FirebaseMessagingException {
        sendPushNotification(notification, notification.getDriverId(), passengerName, NotificationType.MEETING, "Pasażer przyszedł do punktu spotkania.", " czeka na Ciebie.");
    }
    public void sendDriverAtMeetingPointNotification(Notification notification, String driverName) throws FirebaseMessagingException {
        sendPushNotification(notification, notification.getPassengerId(), driverName, NotificationType.MEETING, "Kierowca jest już na miejscu.", " czeka na Ciebie.");
    }
}