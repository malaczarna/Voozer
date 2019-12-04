package pl.jarosyjarosy.yougetin.trip.model;

import com.google.common.base.Objects;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "trips")
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date createDate;
    private Long passengerId;
    private Long driverId;
    private Double meetingLat;
    private Double meetingLng;
    private Double destinationLat;
    private Double destinationLng;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
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

    public Double getDestinationLat() {
        return destinationLat;
    }

    public void setDestinationLat(Double destinationLat) {
        this.destinationLat = destinationLat;
    }

    public Double getDestinationLng() {
        return destinationLng;
    }

    public void setDestinationLng(Double destinationLng) {
        this.destinationLng = destinationLng;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trip trip = (Trip) o;
        return Objects.equal(id, trip.id) &&
                Objects.equal(passengerId, trip.passengerId) &&
                Objects.equal(driverId, trip.driverId) &&
                Objects.equal(meetingLat, trip.meetingLat) &&
                Objects.equal(meetingLng, trip.meetingLng) &&
                Objects.equal(destinationLat, trip.destinationLat) &&
                Objects.equal(destinationLng, trip.destinationLng);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, passengerId, driverId, meetingLat, meetingLng, destinationLat, destinationLng);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("id", id)
                .add("passengerId", passengerId)
                .add("driverId", driverId)
                .add("meetingLat", meetingLat)
                .add("meetingLng", meetingLng)
                .add("destinationLat", destinationLat)
                .add("destinationLng", destinationLng)
                .toString();
    }
}
