package pl.jarosyjarosy.yougetin.trip.service;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import pl.jarosyjarosy.yougetin.trip.endpoint.model.TripMessage;
import pl.jarosyjarosy.yougetin.trip.model.Trip;

@Component
public class TripMapperService {

    public TripMessage mapTrip(Trip trip) {
        TripMessage tripMessage = new TripMessage();
        BeanUtils.copyProperties(trip, tripMessage);

        return tripMessage;
    }

    public Trip mapTripMessage(TripMessage tripMessage) {
        Trip trip = new Trip();
        BeanUtils.copyProperties(tripMessage, trip);

        return trip;
    }
}
