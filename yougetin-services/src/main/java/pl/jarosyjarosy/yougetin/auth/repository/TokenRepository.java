package pl.jarosyjarosy.yougetin.auth.repository;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import pl.jarosyjarosy.yougetin.auth.model.Token;

import javax.transaction.Transactional;
import java.util.List;

@Qualifier("tokens")
@Repository
@Transactional
public interface TokenRepository extends CrudRepository<Token, Long> {
    List<Token> findByToken(String token);
}
