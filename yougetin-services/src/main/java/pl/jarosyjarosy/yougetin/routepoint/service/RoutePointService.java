package pl.jarosyjarosy.yougetin.routepoint.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.jarosyjarosy.yougetin.destination.model.Destination;
import pl.jarosyjarosy.yougetin.rest.RecordNotFoundException;
import pl.jarosyjarosy.yougetin.routepoint.model.RoutePoint;
import pl.jarosyjarosy.yougetin.routepoint.repository.RoutePointRepository;

import javax.transaction.Transactional;
import java.time.Clock;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class RoutePointService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RoutePointService.class);

    private final RoutePointRepository routePointRepository;
    private final Clock clock;

    @Autowired
    public RoutePointService(RoutePointRepository routePointRepository,
                             Clock clock) {
        this.routePointRepository = routePointRepository;
        this.clock = clock;
    }

    public RoutePoint get(Long id) {
        LOGGER.info("LOGGER: get routePoint {}", id);
        if (routePointRepository.findById(id).isPresent()) {
            return routePointRepository.findById(id).get();
        } else {
            throw new RecordNotFoundException();
        }
    }
    @Transactional
    public List<RoutePoint> saveRoute(List<RoutePoint> points, Long oldDestinationId) {
        LOGGER.info("LOGGER: saving routePoints for destination {}", points.get(0).getDestinationId());
        deleteRoutePoints(oldDestinationId);
        Date now = Date.from(clock.instant());
        for (RoutePoint point : points) {
            point.setCreateDate(now);
        }
        Iterable<RoutePoint> iterable = routePointRepository.saveAll(points);
        List<RoutePoint> savedPoints = new ArrayList<>();
        for (RoutePoint point: iterable) {
            savedPoints.add(point);
        }

        return savedPoints;
    }

    public List<RoutePoint> getRoute(Long destinationId) {
        return routePointRepository.findAllByDestinationId(destinationId);
    }

    private void deleteRoutePoints(Long destinationId) {
        routePointRepository.deleteAllByDestinationId(destinationId);
    }
}
