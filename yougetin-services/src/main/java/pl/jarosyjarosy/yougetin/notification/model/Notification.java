package pl.jarosyjarosy.yougetin.notification.model;

import com.google.common.base.Objects;

import javax.persistence.*;

@Entity
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long passengerId;
    private Long driverId;

    private Double meetingLat;
    private Double meetingLng;

    private boolean atMeetingPoint;

    private NotificationType type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(Long passengerId) {
        this.passengerId = passengerId;
    }

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    public Double getMeetingLat() {
        return meetingLat;
    }

    public void setMeetingLat(Double meetingLat) {
        this.meetingLat = meetingLat;
    }

    public Double getMeetingLng() {
        return meetingLng;
    }

    public void setMeetingLng(Double meetingLng) {
        this.meetingLng = meetingLng;
    }

    public boolean isAtMeetingPoint() {
        return atMeetingPoint;
    }

    public void setAtMeetingPoint(boolean atMeetingPoint) {
        this.atMeetingPoint = atMeetingPoint;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Notification that = (Notification) o;
        return Objects.equal(id, that.id) &&
                Objects.equal(passengerId, that.passengerId) &&
                Objects.equal(driverId, that.driverId) &&
                Objects.equal(meetingLat, that.meetingLat) &&
                Objects.equal(meetingLng, that.meetingLng);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, passengerId, driverId, meetingLat, meetingLng);
    }
}
