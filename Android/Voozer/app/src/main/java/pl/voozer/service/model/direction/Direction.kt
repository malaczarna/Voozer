package pl.voozer.service.model.direction

import com.google.gson.annotations.SerializedName

data class Direction (

	@SerializedName("geocoded_waypoints") val geocoded_waypoints : List<GeocodedWaypoints>,
	@SerializedName("routes") val routes : List<Routes>,
	@SerializedName("status") val status : String
)