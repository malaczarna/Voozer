package pl.jarosyjarosy.yougetin.user.service;

import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.geotools.referencing.GeodeticCalculator;
import org.locationtech.jts.geom.Coordinate;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.TransformException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.jarosyjarosy.yougetin.destination.model.Destination;
import pl.jarosyjarosy.yougetin.destination.service.DestinationService;
import pl.jarosyjarosy.yougetin.rest.RecordNotFoundException;
import pl.jarosyjarosy.yougetin.routepoint.model.RoutePoint;
import pl.jarosyjarosy.yougetin.routepoint.service.RoutePointService;
import pl.jarosyjarosy.yougetin.stop.model.Stop;
import pl.jarosyjarosy.yougetin.stop.repository.StopRepository;
import pl.jarosyjarosy.yougetin.stop.service.StopService;
import pl.jarosyjarosy.yougetin.user.endpoint.message.Position;
import pl.jarosyjarosy.yougetin.user.model.Profile;
import pl.jarosyjarosy.yougetin.user.model.Role;
import pl.jarosyjarosy.yougetin.user.model.User;
import pl.jarosyjarosy.yougetin.user.repository.RoleRepository;
import pl.jarosyjarosy.yougetin.user.repository.UserRepository;
import sun.security.krb5.internal.crypto.Des;

import javax.transaction.Transactional;
import java.time.Clock;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private static final String EPSG4326 = "GEOGCS[\"WGS 84\",DATUM[\"WGS_1984\",SPHEROID[\"WGS 84\",6378137,298.257223563,AUTHORITY[\"EPSG\",\"7030\"]],AUTHORITY[\"EPSG\",\"6326\"]],PRIMEM[\"Greenwich\",0,AUTHORITY[\"EPSG\",\"8901\"]],UNIT[\"degree\",0.01745329251994328,AUTHORITY[\"EPSG\",\"9122\"]],AUTHORITY[\"EPSG\",\"4326\"]]";

    private UserRepository userRepository;
    private UserValidationService userValidationService;
    private RoleRepository roleRepository;
    private StopRepository stopRepository;
    private RoutePointService routePointService;
    private Clock clock;

    @Autowired
    public UserService(UserRepository userRepository,
                       UserValidationService userValidationService,
                       RoleRepository roleRepository,
                       StopRepository stopRepository,
                       RoutePointService routePointService,
                       Clock clock) {
        this.userRepository = userRepository;
        this.userValidationService = userValidationService;
        this.roleRepository = roleRepository;
        this.stopRepository = stopRepository;
        this.routePointService = routePointService;
        this.clock = clock;
    }

    public User get(Long id) {
        LOGGER.info("LOGGER: get user {}", id);
        if (userRepository.findById(id).isPresent()) {
            return userRepository.findById(id).get();
        } else {
            throw new RecordNotFoundException();
        }
    }

    @Transactional
    public User validateAndCreate(User user, List<Role> roles) {
        LOGGER.info("LOGGER: validate and create user {}", user.getEmail());

        userValidationService.assureUserWithUniqueEmail(user);
        userValidationService.assureAtLeastOneRoleIsDefined(roles);
        userValidationService.assureRolesAreNotDoubled(roles);

        assureDefaultFieldsAreNotEmpty(user);
        user.setCreateDate(Date.from(clock.instant()));
        final User newUser = userRepository.save(user);

        roles.forEach(role -> {
            role.setUserId(newUser.getId());
            roleRepository.save(role);
        });

        return newUser;
    }

    public List<Role> getRoles(Long userId) {
        return roleRepository.findByUserId(userId);
    }

    private void assureDefaultFieldsAreNotEmpty(User user) {
        if (user.getActive() == null) {
            user.setActive(true);
        }

        if (user.getCurrentProfile() == null) {
            user.setCurrentProfile(Profile.PASSENGER);
        }

        if (user.getBlocked() == null) {
            user.setBlocked(false);
        }

        if (user.getBlocked()) {
            user.setActive(false);
        }
    }

    public Position getUserPosition(Long id) {
        LOGGER.info("LOGGER: get user {} routepoint", id);
        User user = get(id);

        return new Position(user.getLat(), user.getLng());
    }

    @Transactional
    public User setUserPosition(Long id, Position position) {
        LOGGER.info("LOGGER: set user {} routepoint", id);
        User user = get(id);

        user.setLat(position.getLat());
        user.setLng(position.getLng());

        return userRepository.save(user);
    }

    @Transactional
    public User changeProfile(Long id) {
        LOGGER.info("LOGGER: change user {} profile ", id);
        User user = get(id);
        if (user.getCurrentProfile() != null && user.getCurrentProfile().equals(Profile.PASSENGER)) {
            user.setCurrentProfile(Profile.DRIVER);
        } else {
            user.setCurrentProfile(Profile.PASSENGER);
        }

        return userRepository.save(user);
    }

    public List<User> getUsersWithDifferentProfile(Long id) {
        LOGGER.info("LOGGER: get different then user {} profile ", id);
        User user = get(id);
        if (user.getCurrentProfile() != null && user.getCurrentProfile().equals(Profile.DRIVER)) {
            return userRepository.findAllByCurrentProfile(Profile.PASSENGER);
        } else {
            return userRepository.findAllByCurrentProfile(Profile.DRIVER);
        }
    }

    public List<User> getInactiveUsers() {
        return userRepository.findInactive();
    }

    @Transactional
    public void setUserAsInactive(User user) {
        user.setActive(false);
        user.setDestinationId(-1L);
        userRepository.save(user);
    }

    @Transactional
    public void setLastActivity(Long id) {
        User user = get(id);
        user.setLastActivity(Date.from(clock.instant()));
        user.setActive(true);
        userRepository.save(user);
    }

    public List<User> getSortedActiveDrivers(Destination destination, User passenger) throws TransformException, FactoryException {
        LOGGER.info("LOGGER: get sorted active drivers for passenger {}", passenger.getId());

        List<User> drivers = userRepository.findActiveDrivers();

        Map<User, Double> driverDistanceMap = new HashMap<>();
        for (User driver : drivers) {
            List<RoutePoint> driversRoute = routePointService.getRoute(driver.getDestinationId());
            Double distance = calculateMinimumDistanceToRoute(driversRoute, new Position(passenger.getLat(), passenger.getLng()));
            distance += calculateMinimumDistanceToRoute(driversRoute, new Position(destination.getLat(), destination.getLng()));
            driverDistanceMap.put(driver, distance);
        }

        return driverDistanceMap.entrySet().stream().sorted(Map.Entry.comparingByValue()).map(Map.Entry::getKey).collect(Collectors.toList());
    }

    @Transactional
    public User saveDestination(Destination destination) {
        User user = get(destination.getUserId());
        user.setDestinationId(destination.getId());
        return userRepository.save(user);
    }

    @Transactional
    public User stopDestination(Long id) {
        User user = get(id);
        user.setDestinationId(-1L);
        return userRepository.save(user);
    }

    public Double calculateDistanceBetweenTwoPositions(Position pos1, Position pos2) throws TransformException, FactoryException {
        CoordinateReferenceSystem crs = CRS.parseWKT(EPSG4326);

        GeodeticCalculator gc = new GeodeticCalculator(crs);
        gc.setStartingPosition(JTS.toDirectPosition(new Coordinate(pos1.getLng(), pos1.getLat()), crs));
        gc.setDestinationPosition(JTS.toDirectPosition(new Coordinate(pos2.getLng(), pos2.getLat()), crs));

        return gc.getOrthodromicDistance();
    }

    public Double calculateMinimumDistanceToRoute(List<RoutePoint> driverRoute, Position position) throws TransformException, FactoryException {
        Double distance = -1D;
        Double distanceToCheck;
        for (RoutePoint point : driverRoute) {
            distanceToCheck = calculateDistanceBetweenTwoPositions(new Position(point.getLat(), point.getLng()), position);
            if (distance > distanceToCheck || distance == -1) {
                distance = distanceToCheck;
            }
        }

        return distance;
    }

    private boolean checkIfPassengerWontBeLate(Double distance, RoutePoint point) {
        Date now = Date.from(clock.instant());
        Double secondsToWalk =  distance.longValue()/1.1;
        Date passengerToBeOnPoint = Date.from(now.toInstant().plusSeconds(secondsToWalk.longValue()));

        Date driverToBeOnPoint = Date.from(point.getCreateDate().toInstant().plusSeconds(point.getSeconds()));

        return passengerToBeOnPoint.getTime() < driverToBeOnPoint.getTime();

    }

    public Stop calculateMeetingPoint(List<RoutePoint> driverRoute, Position passengerPosition) throws TransformException, FactoryException {
        if (driverRoute.size() > 0) {

            Position closestDriverPoint = new Position(0D,0D);
            Double distance = -1D;
            Double distanceToCheck;
            for (RoutePoint point : driverRoute) {
                distanceToCheck = calculateDistanceBetweenTwoPositions(new Position(point.getLat(), point.getLng()), passengerPosition);
                if (distance > distanceToCheck || distance == -1) {
//                    if (checkIfPassengerWontBeLate(distanceToCheck, point)) {
                        distance = distanceToCheck;
                        closestDriverPoint = new Position(point.getLat(), point.getLng());
//                    }
                }
            }

            if (closestDriverPoint.equals(new Position(0D, 0D))) {
               throw new MeetingPointNotFoundException("Users cannot meet");
            }

            Stop closestStop = new Stop();
            Double stopDistance = -1D;
            Double stopDistanceToCheck;
            for (Stop stop : stopRepository.findAll()) {
                stopDistanceToCheck = calculateDistanceBetweenTwoPositions(new Position(stop.getLat(), stop.getLng()), closestDriverPoint);
                if (stopDistance > stopDistanceToCheck || stopDistance == -1) {
                    stopDistance = stopDistanceToCheck;
                    closestStop = stop;
                }
            }

            return closestStop;
        }

        return null;
    }

}
