package pl.jarosyjarosy.yougetin.notification.service;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import pl.jarosyjarosy.yougetin.notification.endpoint.model.NotificationMessage;
import pl.jarosyjarosy.yougetin.notification.model.Notification;

@Component
public class NotificationMapperService {

    public NotificationMessage mapNotification(Notification notification) {
        NotificationMessage notificationMessage = new NotificationMessage();
        BeanUtils.copyProperties(notification, notificationMessage);

        return notificationMessage;
    }

    public Notification mapNotificationMessage(NotificationMessage notificationMessage) {
        Notification notification = new Notification();
        BeanUtils.copyProperties(notificationMessage, notification);

        return notification;
    }
}
