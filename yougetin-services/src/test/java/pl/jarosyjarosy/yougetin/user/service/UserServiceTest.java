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
import pl.jarosyjarosy.yougetin.stop.model.Stop;
import pl.jarosyjarosy.yougetin.stop.repository.StopRepository;
import pl.jarosyjarosy.yougetin.user.endpoint.message.Position;
import pl.jarosyjarosy.yougetin.user.model.User;
import pl.jarosyjarosy.yougetin.user.repository.UserRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private StopRepository stopRepository;

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

        RoutePoint point = new RoutePoint(52.38, 16.91, 100L, 100L);
        RoutePoint point2 = new RoutePoint(52.39, 16.91, 200L, 100L);
        RoutePoint point3 = new RoutePoint(52.40, 16.91, 300L, 100L);
        RoutePoint point4 = new RoutePoint(52.41, 16.91, 400L, 100L);
        RoutePoint point5 = new RoutePoint(52.42, 16.91, 500L, 100L);

        Double distance = userService.calculateDistanceBetweenTwoPositions(new Position(52.40, 16.87), new Position(52.40, 16.91));

        assertThat(userService.calculateMinimumDistanceToRoute(Arrays.asList(point, point2, point3, point4, point5), new Position(passenger.getLat(), passenger.getLng()))).isEqualTo(distance);
    }

//    @Test
//    public void shoudReturnExpectedMeetingPoint() throws TransformException, FactoryException {
//        User passenger = new User();
//        passenger.setId(10L);
//        passenger.setLat(52.429945);
//        passenger.setLng(16.921553);
//        passenger.setDestinationId(100L);
//
//        Position expected = new Position(52.43009804, 16.92015299);
//
//        RoutePoint point = new RoutePoint(52.3907, 16.92068, 77L, 100L);
//        RoutePoint point2 = new RoutePoint(52.39072, 16.92049, 67L, 100L);
//        RoutePoint point3 = new RoutePoint(52.39074, 16.9204, 244L, 100L);
//        RoutePoint point4 = new RoutePoint(52.390750000000004, 16.920340000000003, 275L, 100L);
//        RoutePoint point5 = new RoutePoint(52.39077, 16.9203, 576L, 100L);
//        RoutePoint point6 = new RoutePoint(52.390800000000006, 16.92021, 640L, 100L);
//        RoutePoint point7 = new RoutePoint(52.390800000000006, 16.92021, 716L, 100L);
//        RoutePoint point8 = new RoutePoint(52.3909, 16.92032, 752L, 100L);
//        RoutePoint point9 = new RoutePoint(52.39094000000001, 16.920360000000002, 1064L, 100L);
//        RoutePoint point10 = new RoutePoint(52.39101, 16.9204, 1128L, 100L);
//        RoutePoint point11 = new RoutePoint(52.39106, 16.92042, 1298L, 100L);
//        RoutePoint point12 = new RoutePoint(52.391110000000005, 16.920440000000003, 1321L, 100L);
//        RoutePoint point13 = new RoutePoint(52.39115, 16.920450000000002, 1324L, 100L);
//        RoutePoint point14 = new RoutePoint(52.391200000000005, 16.920460000000002, 1378L, 100L);
//        RoutePoint point15 = new RoutePoint(52.39124, 16.92047, 1412L, 100L);
//
//        List<RoutePoint> route = Arrays.asList(point, point2, point3, point4, point5, point6, point7, point8, point9, point10, point11, point12, point13, point14, point15);
//
//        Stop stop = new Stop();
//        stop.setLat(52.43009804);
//        stop.setLng(16.92015299);
//        Stop stop2 = new Stop();
//        stop2.setLat(52.42926717);
//        stop2.setLng(16.91995194);
//        Stop stop3 = new Stop();
//        stop3.setLat(52.39824851);
//        stop3.setLng(16.93481132);
//
//        List<Stop> stops = Arrays.asList(stop, stop2, stop3);
//
//        when(stopRepository.findAll()).thenReturn(stops);
//
//        assertThat(userService.calculateMeetingPoint(route, new Position(passenger.getLat(), passenger.getLng())).getLat()).isEqualTo(expected.getLat());
//        assertThat(userService.calculateMeetingPoint(route, new Position(passenger.getLat(), passenger.getLng())).getLng()).isEqualTo(expected.getLng());
//
//
//    }
}