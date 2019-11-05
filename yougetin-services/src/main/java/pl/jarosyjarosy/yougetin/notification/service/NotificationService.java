package pl.jarosyjarosy.yougetin.notification.service;

import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.jarosyjarosy.yougetin.notification.model.Notification;
import pl.jarosyjarosy.yougetin.notification.repository.NotificationRepository;
import pl.jarosyjarosy.yougetin.rest.RecordNotFoundException;
import pl.jarosyjarosy.yougetin.routepoint.model.RoutePoint;
import pl.jarosyjarosy.yougetin.routepoint.service.RoutePointService;
import pl.jarosyjarosy.yougetin.trip.service.TripService;
import pl.jarosyjarosy.yougetin.user.endpoint.message.Position;
import pl.jarosyjarosy.yougetin.user.model.User;
import pl.jarosyjarosy.yougetin.user.service.UserService;

import java.util.List;

@Component
public class NotificationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TripService.class);

    private NotificationRepository notificationRepository;
    private UserService userService;
    private final RoutePointService routePointService;

    @Autowired
    NotificationService(NotificationRepository notificationRepository,
                        UserService userService,
                        RoutePointService routePointService) {
        this.notificationRepository = notificationRepository;
        this.userService = userService;
        this.routePointService = routePointService;
    }

    public Notification get(Long id) {
        LOGGER.info("LOGGER: get notification {}", id);
        if (notificationRepository.findById(id).isPresent()) {
            return notificationRepository.findById(id).get();
        } else {
            throw new RecordNotFoundException();
        }
    }

    public Notification create(Notification notification) throws TransformException, FactoryException {
        LOGGER.info("LOGGER: create notification from {} to {}", notification.getPassengerId(), notification.getDriverId());
        Position meetingPosiition = calculateMeetingPoint(notification.getDriverId(), notification.getPassengerId());

        notification.setMeetingLat(meetingPosiition.getLat());
        notification.setMeetingLng(meetingPosiition.getLng());

        return notificationRepository.save(notification);
    }

    public Position calculateMeetingPoint(Long driverId, Long passengerId) throws TransformException, FactoryException {
        Position passengerPos = userService.getUserPosition(passengerId);
        User driver = userService.get(driverId);

        RoutePoint closestPoint = new RoutePoint(0D, 0D, 0L);
        List<RoutePoint> driverRoute = routePointService.getRoute(driver.getDestinationId());
        for (RoutePoint point : driverRoute) {
            if (closestPoint.getDestinationId() == 0L || userService.calculateDistanceBetweenTwoPositions(passengerPos, new Position(point.getLat(), point.getLng())) <
                    userService.calculateDistanceBetweenTwoPositions(passengerPos, new Position(closestPoint.getLat(), closestPoint.getLng()))) {
                closestPoint = point;
            }
        }


        return new Position(closestPoint.getLat(), closestPoint.getLng());
    }
}
