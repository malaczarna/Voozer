package pl.jarosyjarosy.yougetin.notification.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.jarosyjarosy.yougetin.notification.endpoint.model.NotificationMessage;
import pl.jarosyjarosy.yougetin.notification.model.Notification;
import pl.jarosyjarosy.yougetin.notification.service.NotificationMapperService;
import pl.jarosyjarosy.yougetin.notification.service.NotificationService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private NotificationService notificationService;
    private NotificationMapperService notificationMapperService;

    @Autowired
    NotificationController(NotificationService notificationService,
                           NotificationMapperService notificationMapperService) {
        this.notificationService = notificationService;
        this.notificationMapperService = notificationMapperService;
    }

    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.GET,
            produces = "application/json"
    )
    public NotificationMessage get(@PathVariable Long id, HttpServletRequest request) {

        return notificationMapperService.mapNotification(notificationService.get(id));
    }

    @RequestMapping(
            method = RequestMethod.POST,
            produces = "application/json"
    )
    public NotificationMessage create(@RequestBody NotificationMessage notificationMessage, HttpServletRequest request) {

        return notificationMapperService.mapNotification(notificationService.create(notificationMapperService.mapNotificationMessage(notificationMessage)));
    }
}
