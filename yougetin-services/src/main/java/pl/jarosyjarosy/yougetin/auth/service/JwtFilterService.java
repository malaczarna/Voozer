package pl.jarosyjarosy.yougetin.auth.service;

import com.google.common.base.Strings;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import pl.jarosyjarosy.yougetin.auth.model.Token;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class JwtFilterService extends GenericFilterBean {
    private static final String AUTHORIZATION_COOKIE_NAME = "authorization";

    private String accessSecretKey;
    private TokenCacheService tokenCacheService;

    @Autowired
    public JwtFilterService(@Value("${auth.accessSecretKey}") String accessSecretKey,
                            TokenCacheService tokenCacheService) {
        this.accessSecretKey = accessSecretKey;
        this.tokenCacheService = tokenCacheService;
    }

    @Override
    public void doFilter(final ServletRequest req,
                         final ServletResponse res,
                         final FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) req;

        if (!request.getRequestURI().equals("/auth/login")
                && !request.getRequestURI().equals("/users/register-command")
                && !request.getMethod().equals("OPTIONS")
                && !request.getRequestURI().startsWith("/error")
                && !request.getRequestURI().startsWith("/favicon.ico")
                && !request.getRequestURI().startsWith("/swagger")
                && !request.getRequestURI().startsWith("/webjars")
                && !request.getRequestURI().startsWith("/v2/api-docs")
                && !request.getRequestURI().startsWith("/csrf")
                && !request.getRequestURI().equals("/")
                && !request.getRequestURI().startsWith("/health")
                && !request.getRequestURI().startsWith("/open")
                && !request.getRequestURI().startsWith("/auth/set-cookie")) {
                filterToken(request, extractTokenFromHeader(request));
        }

        chain.doFilter(req, res);
    }

    private void filterToken(HttpServletRequest request, String token) throws InvalidTokenException {
        assureTokenExists(token, request);

        if (Strings.isNullOrEmpty(token)) {
            throw new InvalidTokenException("Missing Authorization header on request " + request.getRequestURI());
        }

        try {
            final Claims claims = parseToken(token);
            request.setAttribute("claims", claims);
        } catch (SignatureException e) {
            throw new InvalidTokenException("Invalid token on request " + request.getRequestURI());
        }
    }

    private void assureTokenExists(String tokenValue, HttpServletRequest request) throws InvalidTokenException {
        Token token = tokenCacheService.findAndUpdate(tokenValue);
        if (token == null) {
            throw new InvalidTokenException("Missing Authorization token in database on request " + request.getRequestURI() + " and token " + tokenValue);
        }
    }

    private Claims parseToken(String token) {
        String secretKey = accessSecretKey;
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }

    private String extractTokenFromHeader(HttpServletRequest request) throws InvalidTokenException {
        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }

        return authHeader.substring(7); // The part after "Bearer "
    }

    private String extractTokenFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(AUTHORIZATION_COOKIE_NAME)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
