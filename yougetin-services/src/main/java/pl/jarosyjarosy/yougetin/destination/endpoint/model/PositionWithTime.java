package pl.jarosyjarosy.yougetin.destination.endpoint.model;

public class PositionWithTime {
    private Double lat;
    private Double lng;
    private Long seconds;

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

    public PositionWithTime(Double lat, Double lng, Long seconds) {
        this.lat = lat;
        this.lng = lng;
        this.seconds = seconds;
    }
}
