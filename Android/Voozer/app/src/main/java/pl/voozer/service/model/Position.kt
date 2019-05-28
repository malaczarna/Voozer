package pl.voozer.service.model

import com.google.gson.annotations.SerializedName

data class Position (
    @SerializedName("lat")
    val lat: Double,
    @SerializedName("lng")
    val lng: Double
)