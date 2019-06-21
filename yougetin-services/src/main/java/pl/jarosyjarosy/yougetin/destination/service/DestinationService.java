package pl.jarosyjarosy.yougetin.destination.service;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.jarosyjarosy.yougetin.destination.model.Destination;
import pl.jarosyjarosy.yougetin.destination.repository.DestinationRepository;
import pl.jarosyjarosy.yougetin.rest.RecordNotFoundException;
import pl.jarosyjarosy.yougetin.user.service.UserService;

import javax.transaction.Transactional;
import javax.validation.ValidationException;
import java.sql.Date;
import java.time.Clock;
import java.util.List;

@Component
public class DestinationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DestinationService.class);

    private final DestinationRepository destinationRepository;
    private UserService userService;
    private final Clock clock;

    @Autowired
    public DestinationService(DestinationRepository destinationRepository,
                              UserService userService,
                              Clock clock) {
        this.destinationRepository = destinationRepository;
        this.userService = userService;
        this.clock = clock;
    }

    public Destination get(Long id) {
        LOGGER.info("LOGGER: get destination {}", id);
        if (destinationRepository.findById(id).isPresent()) {
            return destinationRepository.findById(id).get();
        } else {
            throw new RecordNotFoundException();
        }
    }

    @Transactional
    public Destination validateAndCreate(Destination destination, Long userId) {
        LOGGER.info("LOGGER: validate and create destination");
        destination.setUserId(userId);
        checkIfFieldsAreNotEmpty(destination);
        destination.setCreateDate(Date.from(clock.instant()));
        final Destination newDestination = destinationRepository.save(destination);
        userService.saveDestination(newDestination);

        return newDestination;
    }

    private void checkIfFieldsAreNotEmpty(Destination destination) {
        if (Strings.isNullOrEmpty(destination.getName()) || destination.getUserId() == null) {
            throw new ValidationException("Empty destination name or user id");
        }

    }

    public List<Destination> getByUserId(Long id) {
        LOGGER.info("LOGGER: get destinations for user {}", id);
        return destinationRepository.getAllByUserId(id);
    }
}
