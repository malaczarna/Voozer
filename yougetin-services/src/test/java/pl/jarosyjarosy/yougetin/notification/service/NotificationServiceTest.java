package pl.jarosyjarosy.yougetin.notification.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;
import pl.jarosyjarosy.yougetin.routepoint.model.RoutePoint;
import pl.jarosyjarosy.yougetin.routepoint.repository.RoutePointRepository;
import pl.jarosyjarosy.yougetin.routepoint.service.RoutePointService;
import pl.jarosyjarosy.yougetin.user.endpoint.message.Position;
import pl.jarosyjarosy.yougetin.user.model.User;
import pl.jarosyjarosy.yougetin.user.service.UserService;

import java.util.Arrays;

import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class NotificationServiceTest {

    @Mock
    private RoutePointRepository routePointRepository;

    @Mock
    private RoutePointService routePointService;

    @Mock
    private UserService userService;

    @InjectMocks
    private NotificationService notificationService;

    @Test
    public void shouldReturnPoint3() throws TransformException, FactoryException {
        User passenger = new User();
        passenger.setId(10L);
        passenger.setLat(52.401810);
        passenger.setLng(16.876383);
        passenger.setDestinationId(100L);

        User driver = new User();
        driver.setId(20L);
        driver.setLat(52.402);
        driver.setLng(16.875);
        driver.setDestinationId(200L);

        RoutePoint point = new RoutePoint(52.43, 16.91, 100L);
        RoutePoint point2 = new RoutePoint(52.42, 16.91, 100L);
        RoutePoint point3 = new RoutePoint(52.41, 16.91, 100L);
        RoutePoint point4 = new RoutePoint(52.40, 16.91, 100L);
        RoutePoint point5 = new RoutePoint(52.39, 16.91, 100L);

        when(userService.get(driver.getId())).thenReturn(driver);
        when(userService.getUserPosition(passenger.getId())).thenReturn(new Position(passenger.getLat(), passenger.getLng()));
        when(userService.calculateDistanceBetweenTwoPositions(new Position(passenger.getLat(), passenger.getLng()), new Position(52.43, 16.91))).thenReturn(300D);
        when(userService.calculateDistanceBetweenTwoPositions(new Position(passenger.getLat(), passenger.getLng()), new Position(52.42, 16.91))).thenReturn(200D);
        when(userService.calculateDistanceBetweenTwoPositions(new Position(passenger.getLat(), passenger.getLng()), new Position(52.41, 16.91))).thenReturn(100D);
        when(userService.calculateDistanceBetweenTwoPositions(new Position(passenger.getLat(), passenger.getLng()), new Position(52.40, 16.91))).thenReturn(250D);
        when(userService.calculateDistanceBetweenTwoPositions(new Position(passenger.getLat(), passenger.getLng()), new Position(52.39, 16.91))).thenReturn(350D);
        when(routePointService.getRoute(200L)).thenReturn(Arrays.asList(point, point2, point3, point4, point5));

        assertThat(notificationService.calculateMeetingPoint(driver.getId(), passenger.getId())).isEqualTo(new Position(point3.getLat(), point3.getLng()));

    }



}