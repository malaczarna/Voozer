package pl.voozer.service.model

import com.google.gson.annotations.SerializedName

data class Auth(@SerializedName("token")
                val token: String)

data class RegisterResponse(@SerializedName("id")
                    val id: Int,
                    @SerializedName("name")
                    val name: String,
                    @SerializedName("email")
                    val email: String,
                    @SerializedName("password")
                    val password: String,
                    @SerializedName("createDate")
                    val createDate: String,
                    @SerializedName("roles")
                    val roles: List<Roles>)