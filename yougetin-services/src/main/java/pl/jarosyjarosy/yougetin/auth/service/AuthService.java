package pl.jarosyjarosy.yougetin.auth.service;

import com.google.common.base.Strings;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pl.jarosyjarosy.yougetin.auth.model.LoginMessage;
import pl.jarosyjarosy.yougetin.auth.model.Token;
import pl.jarosyjarosy.yougetin.auth.repository.TokenRepository;
import pl.jarosyjarosy.yougetin.user.model.Role;
import pl.jarosyjarosy.yougetin.user.model.RoleType;
import pl.jarosyjarosy.yougetin.user.model.User;
import pl.jarosyjarosy.yougetin.user.repository.RoleRepository;
import pl.jarosyjarosy.yougetin.user.repository.UserRepository;
import pl.jarosyjarosy.yougetin.user.service.PasswordService;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordService passwordService;
    private String accessSecretKey;
    private TokenRepository tokenRepository;
    private RoleRepository roleRepository;

    @Autowired
    public AuthService(UserRepository userRepository, PasswordService passwordService,
                       TokenRepository tokenRepository, RoleRepository roleRepository,
                       @Value("${auth.accessSecretKey}") String accessSecretKey) {
        this.userRepository = userRepository;
        this.passwordService = passwordService;
        this.tokenRepository = tokenRepository;
        this.roleRepository = roleRepository;
        this.accessSecretKey = accessSecretKey;
    }

    public String getAuthToken(LoginMessage loginMessage) throws InvalidKeySpecException, NoSuchAlgorithmException {
        if (Strings.isNullOrEmpty(loginMessage.getEmail()) || Strings.isNullOrEmpty(loginMessage.getPassword())) {
            throw new AuthorizationException("Empty login or password");
        }

        User user = userRepository.findByEmail(loginMessage.getEmail());
        String expectedPassword = passwordService.getPasswordHash(loginMessage.getPassword());

        if (user == null || !user.getPassword().equals(expectedPassword) || Objects.equals(user.getBlocked(), true)) {
            throw new AuthorizationException("No such user, password invalid or user blocked");
        }

        List<RoleType> roles =
                roleRepository.findByUserId(user.getId()).stream().map(Role::getType).collect(Collectors.toList());

        String token = Jwts.builder().setSubject(loginMessage.getEmail())
                .claim("roles", roles)
                .claim("id", user.getId())
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, accessSecretKey).compact();

        storeToken(token, user.getId());

        return token;
    }

    private void storeToken(String value, Long userId) {
        Token token = new Token();
        token.setUserId(userId);
        token.setToken(value);
        token.setModifyDate(Date.from(Instant.now()));

        tokenRepository.save(token);
    }
}
