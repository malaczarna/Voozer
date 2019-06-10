package pl.jarosyjarosy.yougetin.auth.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.jarosyjarosy.yougetin.auth.model.Token;
import pl.jarosyjarosy.yougetin.auth.repository.TokenRepository;

import java.time.Clock;
import java.util.Date;
import java.util.List;

@Component
public class TokenService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TokenService.class);
    private TokenRepository tokenRepository;
    private Clock clock;

    @Autowired
    public TokenService(TokenRepository tokenRepository, Clock clock) {
        this.tokenRepository = tokenRepository;
        this.clock = clock;
    }

    public void storeToken(String value, Long userId) {
        LOGGER.info("LOGGER: saving new token for user {}", userId);

        deleteOldTokens(userId);

        Token token = new Token();
        token.setUserId(userId);
        token.setToken(value);
        token.setModifyDate(Date.from(clock.instant()));

        tokenRepository.save(token);
    }

    public void deleteOldTokens(Long userId) {
        List<Token> oldTokens = tokenRepository.findByUserId(userId);
        if (oldTokens.size() > 0) {
            LOGGER.info("LOGGER: deleting old tokens for user {}", userId);
            for (Token oldToken : oldTokens) {
                tokenRepository.delete(oldToken);
            }
        }
    }

}
