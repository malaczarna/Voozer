package pl.voozer.service.model

import com.google.gson.annotations.SerializedName

data class PositionWithTime (
    @SerializedName("lat")
    val lat: Double,
    @SerializedName("lng")
    val lng: Double,
    @SerializedName("seconds")
    val seconds: Double
)