package pl.jarosyjarosy.yougetin.trips.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;
import pl.jarosyjarosy.yougetin.trips.model.Trip;

import javax.transaction.Transactional;
import java.util.List;

@Qualifier("trips")
@Repository
@Transactional
public interface TripRepository extends CrudRepository<Trip, Long>, QueryByExampleExecutor<Trip> {
    public List<Trip> findAllByDriverId(Long id);
    public List<Trip> findAllByPassengerId(Long id);
}
