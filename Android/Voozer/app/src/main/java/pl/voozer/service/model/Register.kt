package pl.voozer.service.model

import com.google.gson.annotations.SerializedName

data class Register(@SerializedName("name")
                    val name: String,
                    @SerializedName("email")
                    val email: String,
                    @SerializedName("password")
                    val password: String,
                    @SerializedName("roles")
                    val roles: List<Roles>)