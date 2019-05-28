package pl.voozer.service.model

import com.google.gson.annotations.SerializedName

data class Roles(@SerializedName("type")
                            val type: String,
                            @SerializedName("active")
                            val active: Boolean)