package pl.jarosyjarosy.yougetin.trip.model;

import com.google.common.base.MoreObjects;
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
    private Long destinationId;
    private Boolean rated;

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

    public Long getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(Long destinationId) {
        this.destinationId = destinationId;
    }

    public Boolean getRated() {
        return rated;
    }

    public void setRated(Boolean rated) {
        this.rated = rated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trip trip = (Trip) o;
        return Objects.equal(id, trip.id) &&
                Objects.equal(createDate, trip.createDate) &&
                Objects.equal(passengerId, trip.passengerId) &&
                Objects.equal(driverId, trip.driverId) &&
                Objects.equal(meetingLat, trip.meetingLat) &&
                Objects.equal(meetingLng, trip.meetingLng) &&
                Objects.equal(destinationId, trip.destinationId) &&
                Objects.equal(rated, trip.rated);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, createDate, passengerId, driverId, meetingLat, meetingLng, destinationId, rated);
    }

    @Override
    public String
    toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("createDate", createDate)
                .add("passengerId", passengerId)
                .add("driverId", driverId)
                .add("meetingLat", meetingLat)
                .add("meetingLng", meetingLng)
                .add("destinationId", destinationId)
                .add("rated", rated)
                .toString();
    }
}
