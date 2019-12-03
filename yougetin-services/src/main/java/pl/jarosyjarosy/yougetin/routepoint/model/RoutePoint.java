package pl.jarosyjarosy.yougetin.routepoint.model;

import javax.persistence.*;

@Entity
@Table(name = "route_points")
public class RoutePoint {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Double lat;
    private Double lng;

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

    public Long getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(Long destinationId) {
        this.destinationId = destinationId;
    }

    public RoutePoint(){ }

    public RoutePoint(Double lat, Double lng, Long destinationId) {
        this.lat = lat;
        this.lng = lng;
        this.destinationId = destinationId;
    }
}
