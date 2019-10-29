package pl.voozer.service.model.direction

import com.google.gson.annotations.SerializedName

data class Duration (

	@SerializedName("text") val text : String,
	@SerializedName("value") val value : Int
)