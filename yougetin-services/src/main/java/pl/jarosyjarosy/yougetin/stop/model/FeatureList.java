package pl.jarosyjarosy.yougetin.stop.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FeatureList {
    @SerializedName("features")
    @Expose
    private List<Feature> features = null;

    public List<Feature> getFeatures() {
        return features;
    }

    public void setFeatures(List<Feature> features) {
        this.features = features;
    }
}
