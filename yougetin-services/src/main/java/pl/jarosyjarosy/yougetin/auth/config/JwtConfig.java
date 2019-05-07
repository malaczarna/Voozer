package pl.jarosyjarosy.yougetin.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.stereotype.Component;
import pl.jarosyjarosy.yougetin.auth.service.JwtFilterService;

@Component
public class JwtConfig extends FilterRegistrationBean {

    @Autowired
    public JwtConfig(JwtFilterService jwtFilter) {
        setFilter(jwtFilter);
    }
}