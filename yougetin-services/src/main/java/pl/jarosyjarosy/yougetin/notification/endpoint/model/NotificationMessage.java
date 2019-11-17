package pl.jarosyjarosy.yougetin.notification.endpoint.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class NotificationMessage {
    private Long id;

    private Long passengerId;
    private Long driverId;

    private Double meetingLat;
    private Double meetingLng;

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
}
