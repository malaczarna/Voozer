package pl.jarosyjarosy.yougetin.notification.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.jarosyjarosy.yougetin.notification.model.Notification;
import pl.jarosyjarosy.yougetin.notification.repository.NotificationRepository;
import pl.jarosyjarosy.yougetin.rest.RecordNotFoundException;
import pl.jarosyjarosy.yougetin.trip.service.TripService;
import pl.jarosyjarosy.yougetin.user.endpoint.message.Position;

@Component
public class NotificationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TripService.class);

    private NotificationRepository notificationRepository;

    @Autowired
    NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public Notification get(Long id) {
        LOGGER.info("LOGGER: get notification {}", id);
        if(notificationRepository.findById(id).isPresent()) {
            return notificationRepository.findById(id).get();
        } else {
            throw new RecordNotFoundException();
        }
    }

    public Notification create(Notification notification) {
        LOGGER.info("LOGGER: create notification from {} to {}", notification.getPassengerId(), notification.getDriverId());
        Position meetingPosiition = calculateMeetingPoint(notification.getDriverId(), notification.getPassengerId());

        notification.setMeetingLat(meetingPosiition.getLat());
        notification.setMeetingLng(meetingPosiition.getLng());

        return notificationRepository.save(notification);
    }

    public Position calculateMeetingPoint(Long driverId, Long passengerId) {


        return new Position(16.1, 52.1);
    }
}
