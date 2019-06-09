package pl.jarosyjarosy.yougetin.user.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import pl.jarosyjarosy.yougetin.user.model.Profile;
import pl.jarosyjarosy.yougetin.user.model.User;

import javax.transaction.Transactional;
import java.util.List;

@Qualifier("users")
@Repository
@Transactional
public interface UserRepository extends CrudRepository<User, Long>, QueryByExampleExecutor<User> {

    User findByEmail(String email);

    List<User> findAllByCurrentProfile(Profile profile);


}