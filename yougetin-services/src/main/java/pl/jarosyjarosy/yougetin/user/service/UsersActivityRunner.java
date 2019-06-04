package pl.jarosyjarosy.yougetin.user.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UsersActivityRunner implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(UsersActivityRunner.class);

    private static final Long SLEEP_TIME_AFTER_USER_CHECKING = 60000L;

    private UserService userService;

    UsersActivityRunner(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run() {
        while (true) {
            try {
                userService.getInactiveUsers().forEach(userService::setUserAsInactive);
                Thread.sleep(SLEEP_TIME_AFTER_USER_CHECKING);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                LOGGER.error("LOGGER: Error while processing user activity checker {}", e.getMessage(), e);
                try {
                    Thread.sleep(SLEEP_TIME_AFTER_USER_CHECKING);
                } catch (InterruptedException e1) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
