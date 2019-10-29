package pl.jarosyjarosy.yougetin.user.endpoint.message;

public class Position {
    private Double Lat;
    private Double Lng;

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

    public Position(Double lat, Double lng) {
        Lat = lat;
        Lng = lng;
    }
}
