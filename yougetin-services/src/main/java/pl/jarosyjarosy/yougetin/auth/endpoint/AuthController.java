package pl.jarosyjarosy.yougetin.auth.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.jarosyjarosy.yougetin.auth.model.LoginMessage;
import pl.jarosyjarosy.yougetin.auth.service.AuthService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @RequestMapping(
            value = "/login",
            method = RequestMethod.POST,
            consumes = "application/json",
            produces = "application/json"
    )
    public String login(@RequestBody final LoginMessage login) throws InvalidKeySpecException, NoSuchAlgorithmException {
        return authService.getAuthToken(login);
    }

    @RequestMapping(
            value = "/set-cookie",
            method = RequestMethod.GET
    )
    public void setCookie(@RequestParam("value") String value, @RequestParam("expiry") int expiry, HttpServletResponse response) {

        Cookie cookie = new Cookie("authorization", value);
        cookie.setMaxAge(expiry);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}