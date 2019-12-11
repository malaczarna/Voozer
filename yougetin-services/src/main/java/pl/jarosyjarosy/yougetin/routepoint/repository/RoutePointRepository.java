package pl.jarosyjarosy.yougetin.routepoint.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;
import pl.jarosyjarosy.yougetin.routepoint.model.RoutePoint;

import javax.transaction.Transactional;
import java.util.List;

@Qualifier("route_points")
@Repository
@Transactional
public interface RoutePointRepository extends CrudRepository<RoutePoint, Long>, QueryByExampleExecutor<RoutePoint> {
    List<RoutePoint> findAllByDestinationId(Long destinationId);

    void deleteAllByDestinationId(Long id);
}
