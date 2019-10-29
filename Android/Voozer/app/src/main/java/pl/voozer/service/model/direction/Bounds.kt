package pl.voozer.service.model.direction

import com.google.gson.annotations.SerializedName


data class Bounds (

	@SerializedName("northeast") val northeast : Northeast,
	@SerializedName("southwest") val southwest : Southwest
)