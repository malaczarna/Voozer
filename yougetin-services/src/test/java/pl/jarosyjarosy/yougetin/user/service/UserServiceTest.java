package pl.jarosyjarosy.yougetin.user.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;
import pl.jarosyjarosy.yougetin.routepoint.model.RoutePoint;
import pl.jarosyjarosy.yougetin.routepoint.service.RoutePointService;
import pl.jarosyjarosy.yougetin.user.endpoint.message.Position;
import pl.jarosyjarosy.yougetin.user.model.User;
import pl.jarosyjarosy.yougetin.user.repository.UserRepository;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoutePointService routePointService;

    @InjectMocks
    private UserService userService;

    @Test
    public void shouldReturnCorrectDistanceToRoute() throws TransformException, FactoryException {
        User passenger = new User();
        passenger.setId(10L);
        passenger.setLat(52.40);
        passenger.setLng(16.87);
        passenger.setDestinationId(100L);

        RoutePoint point = new RoutePoint(52.38, 16.91, 100L);
        RoutePoint point2 = new RoutePoint(52.39, 16.91, 100L);
        RoutePoint point3 = new RoutePoint(52.40, 16.91, 100L);
        RoutePoint point4 = new RoutePoint(52.41, 16.91, 100L);
        RoutePoint point5 = new RoutePoint(52.42, 16.91, 100L);

        Double distance = userService.calculateDistanceBetweenTwoPositions(new Position(52.40, 16.87), new Position(52.40, 16.91));

        assertThat(userService.calculateMinimumDistanceToRoute(Arrays.asList(point, point2, point3, point4, point5), new Position(passenger.getLat(), passenger.getLng()))).isEqualTo(distance);
    }
}