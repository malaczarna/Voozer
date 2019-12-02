package pl.jarosyjarosy.yougetin.notification.service;

import com.google.firebase.messaging.FirebaseMessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.jarosyjarosy.yougetin.fcm.service.FCMService;
import pl.jarosyjarosy.yougetin.notification.model.Notification;
import pl.jarosyjarosy.yougetin.notification.model.NotificationType;
import pl.jarosyjarosy.yougetin.notification.repository.NotificationRepository;
import pl.jarosyjarosy.yougetin.rest.RecordNotFoundException;
import pl.jarosyjarosy.yougetin.trip.service.TripService;
import pl.jarosyjarosy.yougetin.user.endpoint.message.Position;
import pl.jarosyjarosy.yougetin.user.model.Profile;
import pl.jarosyjarosy.yougetin.user.model.User;
import pl.jarosyjarosy.yougetin.user.service.UserService;


@Component
public class NotificationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TripService.class);

    private NotificationRepository notificationRepository;
    private UserService userService;
    private FCMService fcmService;

    @Autowired
    NotificationService(NotificationRepository notificationRepository,
                        UserService userService,
                        FCMService fcmService) {
        this.notificationRepository = notificationRepository;
        this.userService = userService;
        this.fcmService = fcmService;
    }

    public Notification get(Long id) {
        LOGGER.info("LOGGER: get notification {}", id);
        if (notificationRepository.findById(id).isPresent()) {
            return notificationRepository.findById(id).get();
        } else {
            throw new RecordNotFoundException();
        }
    }

    public Notification send(Notification notification, Long userId) throws FirebaseMessagingException {
        User user = userService.get(userId);
        Notification baseNotification = notificationRepository.getByDriverIdAndPassengerId(notification.getDriverId(), notification.getPassengerId());
        if (notification.getType().equals(NotificationType.ASK)) {
            return creteAndSend(notification);
        }
        if (notification.getType().equals(NotificationType.ACCEPT)) {
            acceptRequest(baseNotification);
            return notification;
        }
        if (notification.getType().equals(NotificationType.DECLINE) && user.getCurrentProfile().equals(Profile.DRIVER)) {
            declineRequest(baseNotification);
            return notification;
        }
        if (notification.getType().equals(NotificationType.DECLINE) && user.getCurrentProfile().equals(Profile.PASSENGER)) {
            stopRequest(baseNotification);
            return notification;
        }

        return null;

    }

    private Notification creteAndSend(Notification notification) throws FirebaseMessagingException {
        LOGGER.info("LOGGER: create notification from {} to {}", notification.getPassengerId(), notification.getDriverId());
        Position meetingPosiition = calculateMeetingPoint(notification.getDriverId(), notification.getPassengerId());

        notification.setMeetingLat(meetingPosiition.getLat());
        notification.setMeetingLng(meetingPosiition.getLng());

        Notification savedNotification = notificationRepository.save(notification);

        fcmService.sendPushNotificationToDriver(savedNotification, this.userService.get(notification.getPassengerId()).getName());

        return savedNotification;
    }

    public Position calculateMeetingPoint(Long driverId, Long passengerId) {
        Position passengerPos = userService.getUserPosition(passengerId);
        User driver = userService.get(driverId);

        return new Position(52.41, 16.91);
    }

    private void declineRequest(Notification notification) throws FirebaseMessagingException {
        LOGGER.info("LOGGER: send decline from {} to {}", notification.getDriverId(), notification.getPassengerId());

        notificationRepository.deleteById(notification.getId());

        fcmService.sendDeclineToPassenger(notification, this.userService.get(notification.getDriverId()).getName());
    }

    private void acceptRequest(Notification notification) throws FirebaseMessagingException {
        LOGGER.info("LOGGER: send accept from {} to {}", notification.getDriverId(), notification.getPassengerId());

        userService.setTravelingId(notification.getDriverId(), notification.getPassengerId());
        notificationRepository.deleteById(notification.getId());

        fcmService.sendAcceptanceToPassenger(notification, this.userService.get(notification.getDriverId()).getName());
    }

    private void stopRequest(Notification notification) throws FirebaseMessagingException {
        LOGGER.info("LOGGER: stop request from {} to {}", notification.getDriverId(), notification.getPassengerId());

        notificationRepository.deleteById(notification.getId());

        fcmService.sendStopRequestNotification(notification, this.userService.get(notification.getPassengerId()).getName());
    }
}
