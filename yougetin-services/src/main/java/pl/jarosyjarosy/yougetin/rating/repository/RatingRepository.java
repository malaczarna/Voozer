package pl.jarosyjarosy.yougetin.rating.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import pl.jarosyjarosy.yougetin.rating.model.Rating;

import java.util.List;

public interface RatingRepository extends CrudRepository<Rating, Long>, QueryByExampleExecutor<Rating> {
    List<Rating> getAllByGivingId(Long id);
    List<Rating> getAllByDriverId(Long id);
    List<Rating> getAllByPassengerId(Long id);
    @Query(value = "select * from ratings", nativeQuery = true)
    List<Rating> getAll();
}
