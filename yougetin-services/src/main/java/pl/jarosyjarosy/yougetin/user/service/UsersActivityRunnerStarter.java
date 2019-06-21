package pl.jarosyjarosy.yougetin.user.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class UsersActivityRunnerStarter {
    private static final Logger LOGGER = LoggerFactory.getLogger(UsersActivityRunnerStarter.class);

    private UserService userService;

    @Autowired
    public UsersActivityRunnerStarter(UserService userService) {
        this.userService = userService;
    }

    @PostConstruct
    public void startsUsersActivityRunner() {
            LOGGER.info("LOGGER: Start users activity runner process");
            new Thread(new UsersActivityRunner(userService)).start();
    }
}
