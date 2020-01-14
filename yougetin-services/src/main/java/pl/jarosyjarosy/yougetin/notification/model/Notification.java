package pl.jarosyjarosy.yougetin.notification.model;

import com.google.common.base.MoreObjects;
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
    private String meetingName;

    private boolean atMeetingPoint;
    @Enumerated(EnumType.STRING)
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

    public String getMeetingName() {
        return meetingName;
    }

    public void setMeetingName(String meetingName) {
        this.meetingName = meetingName;
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
        return atMeetingPoint == that.atMeetingPoint &&
                Objects.equal(id, that.id) &&
                Objects.equal(passengerId, that.passengerId) &&
                Objects.equal(driverId, that.driverId) &&
                Objects.equal(meetingLat, that.meetingLat) &&
                Objects.equal(meetingLng, that.meetingLng) &&
                Objects.equal(meetingName, that.meetingName) &&
                type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, passengerId, driverId, meetingLat, meetingLng, meetingName, atMeetingPoint, type);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("passengerId", passengerId)
                .add("driverId", driverId)
                .add("meetingLat", meetingLat)
                .add("meetingLng", meetingLng)
                .add("meetingName", meetingName)
                .add("atMeetingPoint", atMeetingPoint)
                .add("type", type)
                .toString();
    }
}
