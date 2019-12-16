package pl.jarosyjarosy.yougetin.notification.service;

import com.google.firebase.messaging.FirebaseMessagingException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.jarosyjarosy.yougetin.destination.model.Destination;
import pl.jarosyjarosy.yougetin.destination.service.DestinationService;
import pl.jarosyjarosy.yougetin.fcm.service.FCMService;
import pl.jarosyjarosy.yougetin.notification.model.Notification;
import pl.jarosyjarosy.yougetin.notification.model.NotificationType;
import pl.jarosyjarosy.yougetin.notification.repository.NotificationRepository;
import pl.jarosyjarosy.yougetin.rest.RecordNotFoundException;
import pl.jarosyjarosy.yougetin.routepoint.service.RoutePointService;
import pl.jarosyjarosy.yougetin.trip.model.Trip;
import pl.jarosyjarosy.yougetin.trip.service.TripService;
import pl.jarosyjarosy.yougetin.user.endpoint.message.Position;
import pl.jarosyjarosy.yougetin.user.model.Profile;
import pl.jarosyjarosy.yougetin.user.model.User;
import pl.jarosyjarosy.yougetin.user.service.UserService;

import java.util.List;


@Component
public class NotificationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TripService.class);

    private NotificationRepository notificationRepository;
    private TripService tripService;
    private UserService userService;
    private RoutePointService routePointService;
    private FCMService fcmService;

    @Autowired
    NotificationService(NotificationRepository notificationRepository,
                        TripService tripService,
                        UserService userService,
                        RoutePointService routePointService,
                        FCMService fcmService) {
        this.notificationRepository = notificationRepository;
        this.tripService = tripService;
        this.userService = userService;
        this.routePointService = routePointService;
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

    public Notification send(Notification notification, Long userId) throws FirebaseMessagingException, TransformException, FactoryException {
        User user = userService.get(userId);
        List<Notification> baseNotification = notificationRepository.getAllByDriverIdAndPassengerIdOrderByIdDesc(notification.getDriverId(), notification.getPassengerId());
        if (notification.getType().equals(NotificationType.ASK)) {
            return creteAndSend(notification);
        }
        if (notification.getType().equals(NotificationType.ACCEPT)) {
            if (baseNotification.isEmpty()) {
                throw new RecordNotFoundException();
            }
            acceptRequest(baseNotification.get(0));
            return notification;
        }
        if (notification.getType().equals(NotificationType.DECLINE) && user.getCurrentProfile().equals(Profile.DRIVER)) {
            if (baseNotification.isEmpty()) {
                throw new RecordNotFoundException();
            }
            declineRequest(baseNotification.get(0));
            return notification;
        }
        if (notification.getType().equals(NotificationType.DECLINE) && user.getCurrentProfile().equals(Profile.PASSENGER)) {
            if (baseNotification.isEmpty()) {
                throw new RecordNotFoundException();
            }
            stopRequest(baseNotification.get(0));
            return notification;
        }
        if (notification.getType().equals(NotificationType.MEETING) && user.getCurrentProfile().equals(Profile.PASSENGER)) {
            if (baseNotification.isEmpty()) {
                throw new RecordNotFoundException();
            }
            passengerAtMeetingPoint(baseNotification.get(0));
            return notification;
        }
        if (notification.getType().equals(NotificationType.MEETING) && user.getCurrentProfile().equals(Profile.DRIVER)) {
            if (baseNotification.isEmpty()) {
                throw new RecordNotFoundException();
            }
            driverAtMeetingPoint(baseNotification.get(0));
            return notification;
        }

        return null;

    }

    private Notification creteAndSend(Notification notification) throws FirebaseMessagingException, TransformException, FactoryException {
        LOGGER.info("LOGGER: create notification from {} to {}", notification.getPassengerId(), notification.getDriverId());
        Position meetingPosition = calculateMeetingPoint(notification.getDriverId(), notification.getPassengerId());

        notification.setMeetingLat(meetingPosition.getLat());
        notification.setMeetingLng(meetingPosition.getLng());

        Notification savedNotification = notificationRepository.save(notification);

        fcmService.sendPushNotificationToDriver(savedNotification, this.userService.get(notification.getPassengerId()).getName());

        return savedNotification;
    }

    Position calculateMeetingPoint(Long driverId, Long passengerId) throws TransformException, FactoryException {
        LOGGER.info("LOGGER: calculate meeting point for driver {} and passenger {}", driverId, passengerId);
        Position passengerPos = userService.getUserPosition(passengerId);
        User driver = userService.get(driverId);

        return userService.calculateMeetingPoint(routePointService.getRoute(driver.getDestinationId()), passengerPos);
    }

    private void declineRequest(Notification notification) throws FirebaseMessagingException {
        LOGGER.info("LOGGER: send decline from {} to {}", notification.getDriverId(), notification.getPassengerId());

        notificationRepository.deleteById(notification.getId());

        fcmService.sendDeclineToPassenger(notification, this.userService.get(notification.getDriverId()).getName());
    }

    private void acceptRequest(Notification notification) throws FirebaseMessagingException {
        LOGGER.info("LOGGER: send accept from {} to {}", notification.getDriverId(), notification.getPassengerId());

        fcmService.sendAcceptanceToPassenger(notification, this.userService.get(notification.getDriverId()).getName());
    }

    private void stopRequest(Notification notification) throws FirebaseMessagingException {
        LOGGER.info("LOGGER: stop request from {} to {}", notification.getDriverId(), notification.getPassengerId());

        notificationRepository.deleteById(notification.getId());

        fcmService.sendStopRequestNotification(notification, this.userService.get(notification.getPassengerId()).getName());
    }

    private void passengerAtMeetingPoint(Notification notification) throws FirebaseMessagingException {
        LOGGER.info("LOGGER: passenger at meeting point from {} to {}", notification.getDriverId(), notification.getPassengerId());

        if (notification.isAtMeetingPoint()) {
            notificationRepository.deleteById(notification.getId());
            tripService.createFromNotification(notification);
        } else {
            notification.setAtMeetingPoint(true);
            notificationRepository.save(notification);
        }

        fcmService.sendPassengerAtMeetingPointNotification(notification, this.userService.get(notification.getPassengerId()).getName());
    }

    private void driverAtMeetingPoint(Notification notification) throws FirebaseMessagingException {
        LOGGER.info("LOGGER: driver at meeting point from {} to {}", notification.getDriverId(), notification.getPassengerId());

        if (notification.isAtMeetingPoint()) {
            notificationRepository.deleteById(notification.getId());
            tripService.createFromNotification(notification);
        } else {
            notification.setAtMeetingPoint(true);
            notificationRepository.save(notification);
        }

        fcmService.sendDriverAtMeetingPointNotification(notification, this.userService.get(notification.getDriverId()).getName());
    }
}
