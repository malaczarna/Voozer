package pl.jarosyjarosy.yougetin.routepoint.service;

import org.springframework.stereotype.Component;
import pl.jarosyjarosy.yougetin.routepoint.model.RoutePoint;
import pl.jarosyjarosy.yougetin.user.endpoint.message.Position;

import java.util.ArrayList;
import java.util.List;

@Component
public class RoutePointMapperService {

    public List<RoutePoint> mapRouteMesage(List<Position> positions, Long destinationId) {
        List<RoutePoint> routePoints = new ArrayList<>();
        for (Position position: positions) {
            RoutePoint newRoutePoint = new RoutePoint();
            newRoutePoint.setDestinationId(destinationId);
            newRoutePoint.setLat(position.getLat());
            newRoutePoint.setLng(position.getLng());

            routePoints.add(newRoutePoint);
        }

        return routePoints;
    }

    public List<Position> mapRoute(List<RoutePoint> routePoints) {
        List<Position> positions = new ArrayList<>();
        if (routePoints != null && !routePoints.isEmpty()) {
            for (RoutePoint routePoint: routePoints) {
                Position position = new Position();
                position.setLat(routePoint.getLat());
                position.setLng(routePoint.getLng());
                positions.add(position);
            }
        }

        return positions;
    }

}
