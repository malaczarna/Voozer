package pl.jarosyjarosy.yougetin.trip.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TripsRatingRunner implements Runnable{

    private static final Logger LOGGER = LoggerFactory.getLogger(TripsRatingRunner.class);

    private static final Long SLEEP_TIME_AFTER_USER_CHECKING = 60000L;

    private TripService tripService;

    TripsRatingRunner(TripService tripService) {
        this.tripService = tripService;
    }

    @Override
    public void run() {
        while (true) {
            try {
                tripService.getUnratedTripsHourAfterMeeting().forEach(tripService::sendRatingsNotifications);
                Thread.sleep(SLEEP_TIME_AFTER_USER_CHECKING);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                LOGGER.error("LOGGER: Error while processing user activity checker {}", e.getMessage(), e);
                try {
                    Thread.sleep(SLEEP_TIME_AFTER_USER_CHECKING);
                } catch (InterruptedException e1) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
