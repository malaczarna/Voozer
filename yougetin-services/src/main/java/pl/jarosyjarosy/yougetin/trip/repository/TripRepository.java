package pl.jarosyjarosy.yougetin.trip.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;
import pl.jarosyjarosy.yougetin.trip.model.Trip;

import javax.transaction.Transactional;
import java.util.List;

@Qualifier("trip")
@Repository
@Transactional
public interface TripRepository extends CrudRepository<Trip, Long>, QueryByExampleExecutor<Trip> {
    List<Trip> findAllByDriverId(Long id);
    List<Trip> findAllByPassengerId(Long id);
}
