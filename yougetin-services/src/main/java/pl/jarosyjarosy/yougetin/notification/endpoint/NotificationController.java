package pl.jarosyjarosy.yougetin.notification.endpoint;

import com.google.firebase.messaging.FirebaseMessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.jarosyjarosy.yougetin.auth.model.Identity;
import pl.jarosyjarosy.yougetin.notification.endpoint.model.NotificationMessage;
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
            method = RequestMethod.POST,
            produces = "application/json"
    )
    public NotificationMessage send(@RequestBody NotificationMessage notificationMessage, HttpServletRequest request) throws FirebaseMessagingException {

        return notificationMapperService.mapNotification(notificationService.send(notificationMapperService.mapNotificationMessage(notificationMessage), new Identity(request).getUserId()));
    }

}
