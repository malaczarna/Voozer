package pl.jarosyjarosy.yougetin.destination.endpoint.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import pl.jarosyjarosy.yougetin.user.endpoint.message.Position;

import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown=true)
public class DestinationMessage {

    private Long id;
    private String name;
    private Long userId;
    private Date createDate;
    private Double lat;
    private Double lng;
    private List<PositionWithTime> route;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
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

    public List<PositionWithTime> getRoute() {
        return route;
    }

    public void setRoute(List<PositionWithTime> route) {
        this.route = route;
    }
}
