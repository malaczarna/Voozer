package pl.jarosyjarosy.yougetin.stop.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;
import pl.jarosyjarosy.yougetin.stop.model.Stop;

import javax.transaction.Transactional;


@Qualifier("stop")
@Repository
@Transactional
public interface StopRepository extends CrudRepository<Stop, Long>, QueryByExampleExecutor<Stop> {
}
