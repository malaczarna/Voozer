package pl.voozer.service.model.direction

import com.google.gson.annotations.SerializedName

data class Routes (

	@SerializedName("bounds") val bounds : Bounds,
	@SerializedName("copyrights") val copyrights : String,
	@SerializedName("legs") val legs : List<Legs>,
	@SerializedName("overview_polyline") val overview_polyline : OverviewPolyline,
	@SerializedName("summary") val summary : String,
	@SerializedName("warnings") val warnings : List<String>,
	@SerializedName("waypoint_order") val waypoint_order : List<String>
)