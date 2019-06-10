package pl.jarosyjarosy.yougetin.trips.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.jarosyjarosy.yougetin.auth.model.Identity;
import pl.jarosyjarosy.yougetin.trips.endpoint.model.TripMessage;
import pl.jarosyjarosy.yougetin.trips.model.Trip;
import pl.jarosyjarosy.yougetin.trips.service.TripMapperService;
import pl.jarosyjarosy.yougetin.trips.service.TripService;

import javax.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/trips")
public class TripController {

    private final TripService tripService;
    private final TripMapperService tripMapperService;

    @Autowired
    public TripController(TripService tripService,
                          TripMapperService tripMapperService) {
        this.tripService = tripService;
        this.tripMapperService = tripMapperService;
    }

    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.GET,
            produces = "application/json"
    )
    public TripMessage get(@PathVariable Long id, HttpServletRequest request) {

        return tripMapperService.mapTrip(tripService.get(id));
    }

    @RequestMapping(
            method = RequestMethod.POST,
            consumes = "application/json",
            produces = "application/json"
    )
    public TripMessage save(@RequestBody TripMessage trip, HttpServletRequest request) {

        Trip newTrip = tripService.validateAndCreate(tripMapperService.mapTripMessage(trip));

        return tripMapperService.mapTrip(newTrip);
    }

    @RequestMapping(
            value = "/passenger",
            method = RequestMethod.GET,
            produces = "application/json"
    )
    public List<TripMessage> getPassengerTrips(HttpServletRequest request) {

        return tripService.getByPassengerId(new Identity(request).getUserId()).stream()
                .map(tripMapperService::mapTrip)
                .collect(Collectors.toList());
    }

    @RequestMapping(
            value = "/driver",
            method = RequestMethod.GET,
            produces = "application/json"
    )
    public List<TripMessage> getDriverTrips(HttpServletRequest request) {

        return tripService.getByDriverId(new Identity(request).getUserId()).stream()
                .map(tripMapperService::mapTrip)
                .collect(Collectors.toList());
    }
}
