package pl.jarosyjarosy.yougetin.routepoint.model;

import javax.persistence.*;

@Entity
@Table(name = "route_points")
public class RoutePoint {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Double Lat;
    private Double Lng;

    private Long destinationId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getLat() {
        return Lat;
    }

    public void setLat(Double lat) {
        Lat = lat;
    }

    public Double getLng() {
        return Lng;
    }

    public void setLng(Double lng) {
        Lng = lng;
    }

    public Long getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(Long destinationId) {
        this.destinationId = destinationId;
    }
}
