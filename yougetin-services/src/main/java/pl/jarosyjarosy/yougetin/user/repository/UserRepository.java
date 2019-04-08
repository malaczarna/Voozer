package pl.jarosyjarosy.yougetin.user.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Component;
import pl.jarosyjarosy.yougetin.user.model.User;

import javax.transaction.Transactional;

@Component
@Transactional
public interface UserRepository extends CrudRepository<User, Long>, QueryByExampleExecutor<User> {

    User findByEmail(String email);
}