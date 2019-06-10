package pl.voozer.service.model

import com.google.gson.annotations.SerializedName

data class Destination(@SerializedName("name")
                       val name: String,
                       @SerializedName("lat")
                       val lat: Double,
                       @SerializedName("lng")
                       val lng: Double)