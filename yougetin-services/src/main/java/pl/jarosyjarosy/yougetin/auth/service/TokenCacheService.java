package pl.jarosyjarosy.yougetin.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import pl.jarosyjarosy.yougetin.auth.model.Token;
import pl.jarosyjarosy.yougetin.auth.repository.TokenRepository;
import pl.jarosyjarosy.yougetin.user.model.User;
import pl.jarosyjarosy.yougetin.user.repository.UserRepository;
import pl.jarosyjarosy.yougetin.user.service.UserService;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Component
public class TokenCacheService {
    private TokenRepository tokenRepository;
    private UserService userService;

    @Autowired
    public TokenCacheService(TokenRepository tokenRepository, UserService userService) {
        this.tokenRepository = tokenRepository;
        this.userService = userService;
    }

    @Cacheable(value = "tokens", cacheManager = "tokensCacheManager", unless="#result == null")
    public Token findAndUpdate(String tokenString) {
        Token token = tokenRepository.findByToken(tokenString);
        if (token != null) {
            token.setModifyDate(Date.from(Instant.now()));
            tokenRepository.save(token);
            userService.setLastActivity(token.getUserId());

            return token;
        }

        return null;
    }
}
