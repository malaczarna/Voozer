package pl.jarosyjarosy.yougetin.rating.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.jarosyjarosy.yougetin.rating.model.Rating;
import pl.jarosyjarosy.yougetin.rating.model.RatingSearchType;
import pl.jarosyjarosy.yougetin.rating.repository.RatingRepository;
import pl.jarosyjarosy.yougetin.rest.RecordNotFoundException;

import javax.transaction.Transactional;
import java.time.Clock;
import java.util.Date;
import java.util.List;

@Component
public class RatingService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RatingService.class);

    private RatingRepository ratingRepository;
    private final Clock clock;

    @Autowired
    public RatingService(RatingRepository ratingRepository, Clock clock){
        this.ratingRepository = ratingRepository;
        this.clock = clock;
    }

    public Rating get(Long id) {
        LOGGER.info("LOGGER: get rating {}", id);
        if(ratingRepository.findById(id).isPresent()) {
            return ratingRepository.findById(id).get();
        } else {
            throw new RecordNotFoundException();
        }
    }

    @Transactional
    public Rating validateAndCreate(Rating rating, Long userId) {
        LOGGER.info("LOGGER: validate and create rating by user {}", userId);
        rating.setCreateDate(Date.from(clock.instant()));
        rating.setGivingId(userId);

        return ratingRepository.save(rating);
    }

    public List<Rating> search(Long userId, RatingSearchType type) {
        if (type.equals(RatingSearchType.GIVING)) {
            return getRatingsByGivingId(userId);
        }
        if (type.equals(RatingSearchType.PASSENGER)) {
            return getRatingsByPassengerId(userId);
        }
        if (type.equals(RatingSearchType.DRIVER)) {
            return getRatingsByDriverId(userId);
        }

        return ratingRepository.getAll();
    }

    private List<Rating> getRatingsByGivingId(Long userId) {
        LOGGER.info("LOGGER: get ratings by giving id {}", userId);
        return ratingRepository.getAllByGivingId(userId);
    }

    private List<Rating> getRatingsByDriverId(Long userId) {
        LOGGER.info("LOGGER: get ratings by driver id {}", userId);
        return ratingRepository.getAllByDriverId(userId);
    }

    private List<Rating> getRatingsByPassengerId(Long userId) {
        LOGGER.info("LOGGER: get ratings by passenger id {}", userId);
        return ratingRepository.getAllByPassengerId(userId);
    }
}
