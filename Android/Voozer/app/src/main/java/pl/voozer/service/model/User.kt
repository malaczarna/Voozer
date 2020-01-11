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
    @SerializedName("phoneNumber")
    val phoneNumber: String,
    @SerializedName("createDate")
    val createDate: Date,
    @SerializedName("destination")
    var destination: Destination,
    @SerializedName("lat")
    val lat: Double,
    @SerializedName("lng")
    val lng: Double,
    @SerializedName("currentProfile")
    val profile: Profile,
    @SerializedName("carBrand")
    val carBrand: String,
    @SerializedName("carModel")
    val carModel: String,
    @SerializedName("carColor")
    val carColor: String
)