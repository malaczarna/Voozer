package pl.jarosyjarosy.yougetin.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import pl.jarosyjarosy.yougetin.auth.model.Token;
import pl.jarosyjarosy.yougetin.auth.repository.TokenRepository;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Component
public class TokenCacheService {
    private TokenRepository tokenRepository;

    @Autowired
    public TokenCacheService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Cacheable(value = "tokens", cacheManager = "tokensCacheManager", unless="#result == null")
    public Token findAndUpdate(String tokenString) {
        List<Token> tokens = tokenRepository.findByToken(tokenString);
        if (tokens != null && tokens.size() > 0) {
            tokens.get(0).setModifyDate(Date.from(Instant.now()));
            tokenRepository.save(tokens.get(0));

            return tokens.get(0);
        }

        return null;
    }
}
