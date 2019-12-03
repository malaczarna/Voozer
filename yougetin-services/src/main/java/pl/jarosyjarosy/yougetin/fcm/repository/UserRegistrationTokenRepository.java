package pl.jarosyjarosy.yougetin.fcm.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import pl.jarosyjarosy.yougetin.fcm.model.UserRegistrationToken;

import java.util.List;

public interface UserRegistrationTokenRepository extends CrudRepository<UserRegistrationToken, Long>, QueryByExampleExecutor<UserRegistrationToken> {
    @Query(value = "select registration_token from user_registration_tokens where user_id = ?1", nativeQuery = true)
    List<String> findTokensByUserId(Long userId);

    void deleteByUserId(Long userId);
}
