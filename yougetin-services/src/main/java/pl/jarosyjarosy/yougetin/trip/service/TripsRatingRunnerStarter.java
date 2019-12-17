package pl.jarosyjarosy.yougetin.trip.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class TripsRatingRunnerStarter {
    private static final Logger LOGGER = LoggerFactory.getLogger(TripsRatingRunnerStarter.class);

    private TripService tripService;

    @Autowired
    public TripsRatingRunnerStarter(TripService tripService) {
        this.tripService = tripService;
    }

    @PostConstruct
    public void startsUsersActivityRunner() {
        LOGGER.info("LOGGER: Start trips rating runner process");
        new Thread(new TripsRatingRunner(tripService)).start();
    }
}
