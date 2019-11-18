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
    private RoutePointService routePointService;
    private DestinationService destinationService;
    private RoleRepository roleRepository;
    private Clock clock;

    @Autowired
    public UserService(UserRepository userRepository,
                       UserValidationService userValidationService,
                       RoutePointService routePointService,
                       DestinationService destinationService,
                       RoleRepository roleRepository,
                       Clock clock) {
        this.userRepository = userRepository;
        this.userValidationService = userValidationService;
        this.routePointService = routePointService;
        this.destinationService = destinationService;
        this.roleRepository = roleRepository;
        this.clock = clock;
    }

    public User get(Long id) {
        LOGGER.info("LOGGER: get user {}", id);
        if(userRepository.findById(id).isPresent()) {
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

        if (user.getCurrentProfile()  == null) {
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
        if (user.getCurrentProfile() != null && user.getCurrentProfile().equals(Profile.DRIVER)){
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

    public List<User> getActiveDrivers() {
        LOGGER.info("LOGGER: get active drivers");
        return userRepository.findActiveDrivers();
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

    public List<User> getDriversInRadiusInMeters(Long id, Double radius) throws FactoryException, TransformException {
        LOGGER.info("LOGGER: get active drivers in {} radius", radius);
        Position userPosition = getUserPosition(id);

        List<User> driversInRadius = new ArrayList<>();

        for (User driver : getActiveDrivers()) {
            Position driverPosition = new Position(driver.getLat(), driver.getLng());
            Double distance = calculateDistanceBetweenTwoPositions(userPosition, driverPosition);
            LOGGER.info("LOGGER: get active drivers in {} radius - actual driver id {}: {} m", radius, driver.getId(), distance);
            if (distance < radius) {
                driversInRadius.add(driver);
            }
        }

        return sortDriversByCompatibility(get(id), driversInRadius);
    }

    public Double calculateDistanceBetweenTwoPositions(Position pos1, Position pos2) throws TransformException, FactoryException {
        CoordinateReferenceSystem crs = CRS.parseWKT(EPSG4326);

        GeodeticCalculator gc = new GeodeticCalculator(crs);
        gc.setStartingPosition(JTS.toDirectPosition(new Coordinate(pos1.getLng(), pos1.getLat()), crs));
        gc.setDestinationPosition(JTS.toDirectPosition(new Coordinate(pos2.getLng(), pos2.getLat()), crs));

        return gc.getOrthodromicDistance();
    }

    private Double calculateDriverCompatibiltyByRoutes(List<RoutePoint> userRoute, List<RoutePoint> driverRoute) throws TransformException, FactoryException {
        if (userRoute.size() > 0) {
            long score = 0L;
            for (RoutePoint userPoint : userRoute) {
                for (RoutePoint driverPoint : driverRoute) {
                    if (calculateDistanceBetweenTwoPositions(new Position(userPoint.getLat(), userPoint.getLng()),
                            new Position(driverPoint.getLat(), driverPoint.getLng())) < 250D) {
                        score++;
                        break;
                    }
                }
            }
            LOGGER.info("LOGGER: calculate driver compatibilty - {}%", (double) score / userRoute.size() * 100);
            return (double) score / userRoute.size() * 100;
        } else {
            LOGGER.info("LOGGER: calculate driver compatibilty - 0%");
            return 0D;
        }
    }

    private Double getDistanceBetweenDestinationAndRoute(User passenger, List<RoutePoint> driverRoute) throws TransformException, FactoryException {

        Position passengerDestinationPoint = new Position(destinationService.get(passenger.getDestinationId()).getLat(), destinationService.get(passenger.getDestinationId()).getLng());

        return calculateMinimumDistanceToRoute(driverRoute, passengerDestinationPoint);
    }

    private Double getDistanceBetweenPassengerAndRoute(User passenger, List<RoutePoint> driverRoute) throws TransformException, FactoryException {
        return calculateMinimumDistanceToRoute(driverRoute, getUserPosition(passenger.getId()));
    }

    private Double calculateMinimumDistanceToRoute(List<RoutePoint> driverRoute, Position position) throws TransformException, FactoryException {
        Double distance = -1D;
        Double distanceToCheck;
        for (RoutePoint point: driverRoute) {
            distanceToCheck = calculateDistanceBetweenTwoPositions(new Position(point.getLat(), point.getLng()), position);
            if (distance >  distanceToCheck || distance == -1) {
                distance = distanceToCheck;
            }
        }

        return distance;
    }

    private List<User> sortDriversByCompatibility(User passenger, List<User> drivers) throws TransformException, FactoryException {
        HashMap<User, Double> driversMap = new HashMap<>();
        for (User driver : drivers) {
            LOGGER.info("LOGGER: calculating driver {} compatibilty", driver.getId());
            driversMap.put(driver, calculateDriverCompatibiltyByRoutes(routePointService.getRoute(passenger.getDestinationId()),
                    routePointService.getRoute(driver.getDestinationId())));
        }

        return driversMap.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).map(Map.Entry::getKey).collect(Collectors.toList());

    }

}
