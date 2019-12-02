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
        LOGGER.info("Registration token saved for user {}", userRegistrationToken.getUserId());
        return this.userRegistrationTokenRepository.save(userRegistrationToken);
    }

    public void deleteRegistrationTokensByUser(Long userId) {
        LOGGER.info("Registration tokens deleted for user {}", userId);
        this.userRegistrationTokenRepository.deleteByUserId(userId);
    }

    public void sendPushNotificationToDriver(Notification notification, String passengerName) throws FirebaseMessagingException {
        List<String> registrationTokens = userRegistrationTokenRepository.findTokensByUserId(notification.getDriverId());

        if (registrationTokens.size() > 0) {
            Message message = Message.builder()
                    .setAndroidConfig(
                            AndroidConfig.builder()
                                    .setNotification(AndroidNotification.builder().setClickAction("SHOW_MESSAGE").build())
                                    .setPriority(AndroidConfig.Priority.HIGH).build())
                    .setNotification(new com.google.firebase.messaging.Notification(
                            "Ktoś chcę się z Tobą zabrać!",
                            passengerName + " pyta się czy go podwieziesz. Kliknij aby zobaczyć punkt odbioru."))
                    .putData("passengerId", notification.getPassengerId().toString())
                    .putData("driverId", notification.getDriverId().toString())
                    .putData("meetingLat", notification.getMeetingLat().toString())
                    .putData("meetingLng", notification.getMeetingLng().toString())
                    .putData("type", NotificationType.ASK.toString())
                    .setToken(registrationTokens.get(0))
                    .build();
            String response = FirebaseMessaging.getInstance().send(message);
            LOGGER.info("Successfully sent message:" + response);
        } else {
            LOGGER.info("No registration tokens for user {}", notification.getDriverId());
        }
    }

    public void sendDeclineToPassenger(Notification notification, String driverName) throws FirebaseMessagingException {
        List<String> registrationTokens = userRegistrationTokenRepository.findTokensByUserId(notification.getPassengerId());

        if (registrationTokens.size() > 0) {
            Message message = Message.builder()
                    .setAndroidConfig(
                            AndroidConfig.builder()
                                    .setNotification(AndroidNotification.builder().setClickAction("SHOW_MESSAGE").build())
                                    .setPriority(AndroidConfig.Priority.HIGH).build())
                    .setNotification(new com.google.firebase.messaging.Notification(
                            "Kierowca odmówił przejazdu.",
                            driverName + " niestety nie może Cię podwieźć. Poszukaj innego kierowcy."))
                    .putData("passengerId", notification.getPassengerId().toString())
                    .putData("driverId", notification.getDriverId().toString())
                    .putData("meetingLat", notification.getMeetingLat().toString())
                    .putData("meetingLng", notification.getMeetingLng().toString())
                    .putData("type", NotificationType.DECLINE.toString())
                    .setToken(registrationTokens.get(0))
                    .build();
            String response = FirebaseMessaging.getInstance().send(message);
            LOGGER.info("Successfully sent message:" + response);
        } else {
            LOGGER.info("No registration tokens for user {}", notification.getPassengerId());
        }
    }

    public void sendAcceptanceToPassenger(Notification notification, String driverName) throws FirebaseMessagingException {
        List<String> registrationTokens = userRegistrationTokenRepository.findTokensByUserId(notification.getPassengerId());

        if (registrationTokens.size() > 0) {
            Message message = Message.builder()
                    .setAndroidConfig(
                            AndroidConfig.builder()
                                    .setNotification(AndroidNotification.builder().setClickAction("SHOW_MESSAGE").build())
                                    .setPriority(AndroidConfig.Priority.HIGH).build())
                    .setNotification(new com.google.firebase.messaging.Notification(
                            "Kierowca chce cię zabrać!",
                            driverName + " chętnie Cie zabierze. Tylko się nie spóźnij!"))
                    .putData("passengerId", notification.getPassengerId().toString())
                    .putData("driverId", notification.getDriverId().toString())
                    .putData("meetingLat", notification.getMeetingLat().toString())
                    .putData("meetingLng", notification.getMeetingLng().toString())
                    .putData("type", NotificationType.ACCEPT.toString())
                    .setToken(registrationTokens.get(0))
                    .build();
            String response = FirebaseMessaging.getInstance().send(message);
            LOGGER.info("Successfully sent message:" + response);
        } else {
            LOGGER.info("No registration tokens for user {}", notification.getPassengerId());
        }
    }

    public void sendStopRequestNotification(Notification notification, String passengerName) throws FirebaseMessagingException {
        List<String> registrationTokens = userRegistrationTokenRepository.findTokensByUserId(notification.getDriverId());

        if (registrationTokens.size() > 0) {
            Message message = Message.builder()
                    .setAndroidConfig(
                            AndroidConfig.builder()
                                    .setNotification(AndroidNotification.builder().setClickAction("SHOW_MESSAGE").build())
                                    .setPriority(AndroidConfig.Priority.HIGH).build())
                    .setNotification(new com.google.firebase.messaging.Notification(
                            "Pasażer się rozmyślił",
                            passengerName + " już nie chce się z Tobazabrać"))
                    .putData("driverId", notification.getDriverId().toString())
                    .putData("passengerId", notification.getPassengerId().toString())
                    .putData("meetingLat", notification.getMeetingLat().toString())
                    .putData("meetingLng", notification.getMeetingLng().toString())
                    .putData("type", NotificationType.DECLINE.toString())
                    .setToken(registrationTokens.get(0))
                    .build();
            String response = FirebaseMessaging.getInstance().send(message);
            LOGGER.info("Successfully sent message:" + response);
        } else {
            LOGGER.info("No registration tokens for user {}", notification.getPassengerId());
        }
    }

}