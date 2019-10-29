package pl.jarosyjarosy.yougetin.user.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;
import pl.jarosyjarosy.yougetin.routepoint.model.RoutePoint;
import pl.jarosyjarosy.yougetin.routepoint.repository.RoutePointRepository;
import pl.jarosyjarosy.yougetin.routepoint.service.RoutePointService;
import pl.jarosyjarosy.yougetin.user.endpoint.message.Position;
import pl.jarosyjarosy.yougetin.user.model.User;
import pl.jarosyjarosy.yougetin.user.repository.UserRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
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
    public void getDriversInRadiusInKm() throws FactoryException, TransformException {
        User passenger = new User();
        passenger.setId(10L);
        passenger.setLat(52.401810);
        passenger.setLng(16.876383);
        passenger.setDestinationId(100L);

        User driver1 = new User();
        driver1.setId(20L);
        driver1.setLat(52.395817);
        driver1.setLng(16.950125);
        driver1.setDestinationId(200L);
        //80%
        User driver2 = new User();
        driver2.setId(30L);
        driver2.setLat(52.407862);
        driver2.setLng(16.90188);
        driver2.setDestinationId(300L);
        //20%
        User driver3 = new User();
        driver3.setId(40L);
        driver3.setLat(52.407862);
        driver3.setLng(16.90188);
        driver3.setDestinationId(400L);
        //60%
        User driver4 = new User();
        driver4.setId(50L);
        driver4.setLat(52.407862);
        driver4.setLng(16.90188);
        driver4.setDestinationId(500L);

        RoutePoint point = new RoutePoint(52.41, 16.91, 100L);
        RoutePoint point2 = new RoutePoint(52.42, 16.91, 100L);
        RoutePoint point3 = new RoutePoint(52.43, 16.91, 100L);
        RoutePoint point4 = new RoutePoint(52.44, 16.91, 100L);
        RoutePoint point5 = new RoutePoint(52.45, 16.91, 100L);

        RoutePoint pointd2 = new RoutePoint(52.42, 16.91, 300L);
        RoutePoint point2d2 = new RoutePoint(52.43, 16.91, 300L);
        RoutePoint point3d2 = new RoutePoint(52.44, 16.91, 300L);
        RoutePoint point4d2 = new RoutePoint(52.45, 16.91, 300L);
        RoutePoint point5d2 = new RoutePoint(52.46, 16.91, 300L);

        RoutePoint pointd3 = new RoutePoint(52.45, 16.91, 400L);
        RoutePoint point2d3 = new RoutePoint(52.46, 16.91, 400L);
        RoutePoint point3d3 = new RoutePoint(52.47, 16.91, 400L);
        RoutePoint point4d3 = new RoutePoint(52.48, 16.91, 400L);
        RoutePoint point5d3 = new RoutePoint(52.49, 16.91, 400L);

        RoutePoint pointd4 = new RoutePoint(52.43, 16.91, 500L);
        RoutePoint point2d4 = new RoutePoint(52.44, 16.91, 500L);
        RoutePoint point3d4 = new RoutePoint(52.45, 16.91, 500L);
        RoutePoint point4d4 = new RoutePoint(52.46, 16.91, 500L);
        RoutePoint point5d4 = new RoutePoint(52.47, 16.91, 500L);


        when(userRepository.findActiveDrivers()).thenReturn(Arrays.asList(driver1, driver2, driver3, driver4));
        when(userRepository.findById(10L)).thenReturn(Optional.of(passenger));
        when(routePointService.getRoute(100L)).thenReturn(Arrays.asList(point, point2, point3, point4, point5));
        when(routePointService.getRoute(300L)).thenReturn(Arrays.asList(pointd2, point2d2, point3d2, point4d2, point5d2));
        when(routePointService.getRoute(400L)).thenReturn(Arrays.asList(pointd3, point2d3, point3d3, point4d3, point5d3));
        when(routePointService.getRoute(500L)).thenReturn(Arrays.asList(pointd4, point2d4, point3d4, point4d4, point5d4));

        assertThat(userService.getDriversInRadiusInMeters(10L, 3000D)).isEqualTo(Arrays.asList(driver2, driver4, driver3));

    }
}