package pl.voozer.service.model.direction

import com.google.gson.annotations.SerializedName

data class Polyline (

	@SerializedName("points") val points : String
)