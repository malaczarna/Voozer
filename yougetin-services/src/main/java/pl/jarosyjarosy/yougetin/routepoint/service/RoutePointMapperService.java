package pl.jarosyjarosy.yougetin.routepoint.service;

import org.springframework.stereotype.Component;
import pl.jarosyjarosy.yougetin.destination.endpoint.model.PositionWithTime;
import pl.jarosyjarosy.yougetin.routepoint.model.RoutePoint;
import pl.jarosyjarosy.yougetin.user.endpoint.message.Position;

import java.util.ArrayList;
import java.util.List;

@Component
public class RoutePointMapperService {

    public List<RoutePoint> mapRouteMesage(List<PositionWithTime> positions, Long destinationId) {
        List<RoutePoint> routePoints = new ArrayList<>();
        for (PositionWithTime position: positions) {
            RoutePoint newRoutePoint = new RoutePoint(position.getLat(), position.getLng(), position.getSeconds(), destinationId);
            routePoints.add(newRoutePoint);
        }

        return routePoints;
    }

    public List<PositionWithTime> mapRoute(List<RoutePoint> routePoints) {
        List<PositionWithTime> positions = new ArrayList<>();
        if (routePoints != null && !routePoints.isEmpty()) {
            for (RoutePoint routePoint: routePoints) {
                PositionWithTime position = new PositionWithTime(routePoint.getLat(), routePoint.getLng(), routePoint.getSeconds());
                positions.add(position);
            }
        }

        return positions;
    }

}
