package pl.jarosyjarosy.yougetin.routepoint.model;

import com.google.common.base.Objects;

import javax.persistence.*;

@Entity
@Table(name = "route_points")
public class RoutePoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double lat;
    private Double lng;
    private Long seconds;

    private Long destinationId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Long getSeconds() {
        return seconds;
    }

    public void setSeconds(Long seconds) {
        this.seconds = seconds;
    }

    public Long getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(Long destinationId) {
        this.destinationId = destinationId;
    }

    public RoutePoint(){ }

    public RoutePoint(Double lat, Double lng, Long seconds, Long destinationId) {
        this.lat = lat;
        this.lng = lng;
        this.seconds = seconds;
        this.destinationId = destinationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoutePoint point = (RoutePoint) o;
        return Objects.equal(id, point.id) &&
                Objects.equal(lat, point.lat) &&
                Objects.equal(lng, point.lng) &&
                Objects.equal(seconds, point.seconds) &&
                Objects.equal(destinationId, point.destinationId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, lat, lng, seconds, destinationId);
    }
}
