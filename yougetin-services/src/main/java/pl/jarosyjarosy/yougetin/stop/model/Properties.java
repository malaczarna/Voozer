package pl.jarosyjarosy.yougetin.stop.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Properties {

    @SerializedName("zone")
    @Expose
    private String zone;
    @SerializedName("route_type")
    @Expose
    private String routeType;
    @SerializedName("headsigns")
    @Expose
    private String headsigns;
    @SerializedName("stop_name")
    @Expose
    private String stopName;

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getRouteType() {
        return routeType;
    }

    public void setRouteType(String routeType) {
        this.routeType = routeType;
    }

    public String getHeadsigns() {
        return headsigns;
    }

    public void setHeadsigns(String headsigns) {
        this.headsigns = headsigns;
    }

    public String getStopName() {
        return stopName;
    }

    public void setStopName(String stopName) {
        this.stopName = stopName;
    }

}
