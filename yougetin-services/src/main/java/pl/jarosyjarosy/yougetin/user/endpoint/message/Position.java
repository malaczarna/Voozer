package pl.jarosyjarosy.yougetin.user.endpoint.message;

import com.google.common.base.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return Objects.equal(Lat, position.Lat) &&
                Objects.equal(Lng, position.Lng);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(Lat, Lng);
    }
}
