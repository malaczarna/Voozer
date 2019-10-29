package pl.voozer.service.model.direction

import com.google.gson.annotations.SerializedName

data class OverviewPolyline (

	@SerializedName("points") val points : String
)