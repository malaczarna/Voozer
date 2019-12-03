package pl.voozer.service.model

import com.google.gson.annotations.SerializedName


data class NotificationMessage (
    @SerializedName("passengerId")
    val passengerId: Long,
    @SerializedName("driverId")
    val driverId: Long,
    @SerializedName("type")
    val notificationType: NotificationType
)