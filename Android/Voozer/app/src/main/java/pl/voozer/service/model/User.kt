package pl.voozer.service.model

import com.google.gson.annotations.SerializedName
import java.util.*

data class User(
    @SerializedName("id")
    val id: Long,
    @SerializedName("name")
    val name: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("createDate")
    val createDate: Date,
    @SerializedName("destination")
    val destination: Destination,
    @SerializedName("lat")
    val lat: Double,
    @SerializedName("lng")
    val lng: Double,
    @SerializedName("currentProfile")
    val profile: Profile
)