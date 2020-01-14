package pl.jarosyjarosy.yougetin.destination.repository;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;
import pl.jarosyjarosy.yougetin.destination.model.Destination;

import javax.transaction.Transactional;
import java.util.List;

@Qualifier("destinations")
@Repository
@Transactional
public interface DestinationRepository extends CrudRepository<Destination, Long>, QueryByExampleExecutor<Destination> {
    List<Destination> getAllByUserIdOrderByCreateDateDesc(Long userId);
}
