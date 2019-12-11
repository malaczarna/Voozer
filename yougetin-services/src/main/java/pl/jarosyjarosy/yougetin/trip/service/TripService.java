package pl.jarosyjarosy.yougetin.trip.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.jarosyjarosy.yougetin.notification.model.Notification;
import pl.jarosyjarosy.yougetin.rest.RecordNotFoundException;
import pl.jarosyjarosy.yougetin.trip.model.Trip;
import pl.jarosyjarosy.yougetin.trip.repository.TripRepository;
import pl.jarosyjarosy.yougetin.user.service.UserService;

import javax.transaction.Transactional;
import java.sql.Date;
import java.time.Clock;
import java.util.List;

@Component
public class TripService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TripService.class);

    private final TripRepository tripRepository;
    private UserService userService;
    private final Clock clock;

    @Autowired
    public TripService(TripRepository tripRepository,
                       UserService userService,
                       Clock clock) {
        this.tripRepository = tripRepository;
        this.userService = userService;
        this.clock = clock;
    }

    public Trip get(Long id) {
        LOGGER.info("LOGGER: get trip {}", id);
        if(tripRepository.findById(id).isPresent()) {
            return tripRepository.findById(id).get();
        } else {
            throw new RecordNotFoundException();
        }
    }

    @Transactional
    public Trip validateAndCreate(Trip trip) {
        LOGGER.info("LOGGER: validate and create trip");
        trip.setCreateDate(Date.from(clock.instant()));
        final Trip newTrip = tripRepository.save(trip);

        return newTrip;
    }

    public List<Trip> getByDriverId(Long id) {
        LOGGER.info("LOGGER: get trip for driver {}", id);
        return tripRepository.findAllByDriverId(id);
    }

    public List<Trip> getByPassengerId(Long id) {
        LOGGER.info("LOGGER: get trip for passenger {}", id);
        return tripRepository.findAllByPassengerId(id);
    }

    public void createFromNotification(Notification notification) {
        Trip newTrip = new Trip();
        newTrip.setMeetingLat(notification.getMeetingLat());
        newTrip.setMeetingLng(notification.getMeetingLng());
        newTrip.setDriverId(notification.getDriverId());
        newTrip.setPassengerId(notification.getPassengerId());
        newTrip.setDestinationId(userService.get(notification.getPassengerId()).getDestinationId());

        validateAndCreate(newTrip);
    }
}
