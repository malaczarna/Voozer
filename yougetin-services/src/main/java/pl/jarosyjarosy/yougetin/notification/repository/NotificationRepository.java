package pl.jarosyjarosy.yougetin.notification.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;
import pl.jarosyjarosy.yougetin.notification.model.Notification;

import javax.transaction.Transactional;

@Qualifier("notification")
@Repository
@Transactional
public interface NotificationRepository extends CrudRepository<Notification, Long>, QueryByExampleExecutor<Notification> {

    Notification getByDriverIdAndPassengerId(Long driverId, Long passengerId);
}
