package pl.voozer.service.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import pl.voozer.R
import android.content.Intent
import pl.voozer.service.bubble.FloatingService
import pl.voozer.utils.NOTIFICATION_BROADCAST_RECEIVER_ACTION


class NotificationService: FirebaseMessagingService() {
    companion object {
        fun createChannel(context: Context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name = context.getString(R.string.default_notification_channel_name)
                val descriptionText = context.getString(R.string.default_notification_channel_description)
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val mChannel = NotificationChannel( context.getString(R.string.default_notification_channel_id), name, importance)
                mChannel.description = descriptionText
                val notificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(mChannel)
            }
        }
    }
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        val data = remoteMessage.data
        val driverId = data["driverId"]
        val passengerId = data["passengerId"]
        val personName = data["name"]
        val meetingName = data["meetingName"]
        val meetingLat = data["meetingLat"]
        val meetingLng = data["meetingLng"]
        val notificationType = data["type"]
        val intent = Intent(NOTIFICATION_BROADCAST_RECEIVER_ACTION)
        intent.putExtra("driverId", driverId)
        intent.putExtra("passengerId", passengerId)
        intent.putExtra("meetingLat", meetingLat)
        intent.putExtra("meetingLng", meetingLng)
        intent.putExtra("type", notificationType)
        intent.putExtra("name", personName)
        intent.putExtra("meetingName", meetingName)
        this.sendBroadcast(intent)
    }
}