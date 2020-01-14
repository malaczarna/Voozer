package pl.voozer.service.model

import com.google.gson.annotations.SerializedName

data class Register(@SerializedName("name")
                    val name: String,
                    @SerializedName("phoneNumber")
                    val phoneNumber: String,
                    @SerializedName("email")
                    val email: String,
                    @SerializedName("password")
                    val password: String,
                    @SerializedName("carBrand")
                    val carBrand: String = "",
                    @SerializedName("carModel")
                    val carModel: String = "",
                    @SerializedName("carColor")
                    val carColor: String = "",
                    @SerializedName("roles")
                    val roles: List<Roles>)