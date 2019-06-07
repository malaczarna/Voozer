package pl.jarosyjarosy.yougetin.trips.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.jarosyjarosy.yougetin.rest.RecordNotFoundException;
import pl.jarosyjarosy.yougetin.trips.model.Trip;
import pl.jarosyjarosy.yougetin.trips.repository.TripRepository;
import pl.jarosyjarosy.yougetin.user.service.UserService;

import javax.transaction.Transactional;
import java.sql.Date;
import java.time.Clock;
import java.util.List;

@Component
public class TripService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TripService.class);

    private final TripRepository tripRepository;
    private final Clock clock;

    @Autowired
    public TripService(TripRepository tripRepository,
                       Clock clock) {
        this.tripRepository = tripRepository;
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
        LOGGER.info("LOGGER: get trips for driver {}", id);
        return tripRepository.findAllByDriverId(id);
    }

    public List<Trip> getByPassengerId(Long id) {
        LOGGER.info("LOGGER: get trips for passenger {}", id);
        return tripRepository.findAllByPassengerId(id);
    }
}
