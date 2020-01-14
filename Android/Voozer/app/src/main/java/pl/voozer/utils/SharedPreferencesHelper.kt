package pl.voozer.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import pl.voozer.service.model.Auth

class SharedPreferencesHelper {
    companion object {

        fun saveToken(context: Context, auth: Auth){
            val editor: SharedPreferences.Editor = context.getSharedPreferences("123", Context.MODE_PRIVATE).edit()
            editor.putString("token", "Bearer ${auth.token}")
            editor.apply()
        }

        fun getToken(context: Context): String? {
            return context.getSharedPreferences("123", Context.MODE_PRIVATE).getString("token", "")
        }

        fun isMainTheme(context: Context): Boolean? {
            return context.getSharedPreferences("123", Context.MODE_PRIVATE).getBoolean("theme", true)
        }

        fun setMainTheme(context: Context, value: Boolean) {
            val editor: SharedPreferences.Editor = context.getSharedPreferences("123", Context.MODE_PRIVATE).edit()
            editor.putBoolean("theme", value)
            editor.apply()
        }

        fun setLoggedIn(context: Context, value: Boolean) {
            val editor: SharedPreferences.Editor = context.getSharedPreferences("123", Context.MODE_PRIVATE).edit()
            editor.putBoolean("logged-in", value)
            editor.apply()
        }

        fun isLoggedIn(context: Context): Boolean? {
            return context.getSharedPreferences("123", Context.MODE_PRIVATE).getBoolean("logged-in", false)
        }

        fun setFirebaseInit(context: Context, value: Boolean) {
            val editor: SharedPreferences.Editor = context.getSharedPreferences("123", Context.MODE_PRIVATE).edit()
            editor.putBoolean("firebase-first-init", value)
            editor.apply()
        }

        fun isFirebaseInit(context: Context): Boolean? {
            return context.getSharedPreferences("123", Context.MODE_PRIVATE).getBoolean("firebase-first-init", false)
        }

        fun setTripActive(context: Context, value: Boolean) {
            val editor: SharedPreferences.Editor = context.getSharedPreferences("123", Context.MODE_PRIVATE).edit()
            editor.putBoolean("trip-active", value)
            editor.apply()
        }

        fun isTripActive(context: Context): Boolean? {
            return context.getSharedPreferences("123", Context.MODE_PRIVATE).getBoolean("trip-active", false)
        }

        fun getMeetingName(context: Context): String? {
            return context.getSharedPreferences("123", Context.MODE_PRIVATE).getString("meeting-name", null)
        }

        fun setMeetingName(context: Context, value: String?) {
            val editor: SharedPreferences.Editor = context.getSharedPreferences("123", Context.MODE_PRIVATE).edit()
            editor.putString("meeting-name", value)
            editor.apply()
        }

        fun getDestinationLat(context: Context): String? {
            return context.getSharedPreferences("123", Context.MODE_PRIVATE).getString("destination-lat", null)
        }

        fun setDestinationLat(context: Context, value: String?) {
            val editor: SharedPreferences.Editor = context.getSharedPreferences("123", Context.MODE_PRIVATE).edit()
            editor.putString("destination-lat", value)
            editor.apply()
        }

        fun getDestinationLng(context: Context): String? {
            return context.getSharedPreferences("123", Context.MODE_PRIVATE).getString("destination-lng", null)
        }

        fun setDestinationLng(context: Context, value: String?) {
            val editor: SharedPreferences.Editor = context.getSharedPreferences("123", Context.MODE_PRIVATE).edit()
            editor.putString("destination-lng", value)
            editor.apply()
        }

        fun getMeetingLat(context: Context): String? {
            return context.getSharedPreferences("123", Context.MODE_PRIVATE).getString("meeting-lat", null)
        }

        fun setMeetingLat(context: Context, value: String?) {
            val editor: SharedPreferences.Editor = context.getSharedPreferences("123", Context.MODE_PRIVATE).edit()
            editor.putString("meeting-lat", value)
            editor.apply()
        }

        fun getMeetingLng(context: Context): String? {
            return context.getSharedPreferences("123", Context.MODE_PRIVATE).getString("meeting-lng", null)
        }

        fun setMeetingLng(context: Context, value: String?) {
            val editor: SharedPreferences.Editor = context.getSharedPreferences("123", Context.MODE_PRIVATE).edit()
            editor.putString("meeting-lng", value)
            editor.apply()
        }

        fun getPassengerId(context: Context): String? {
            return context.getSharedPreferences("123", Context.MODE_PRIVATE).getString("passenger-id", null)
        }

        fun setPassengerId(context: Context, value: String?) {
            val editor: SharedPreferences.Editor = context.getSharedPreferences("123", Context.MODE_PRIVATE).edit()
            editor.putString("passenger-id", value)
            editor.apply()
        }

        fun getDriverId(context: Context): String? {
            return context.getSharedPreferences("123", Context.MODE_PRIVATE).getString("driver-id", null)
        }

        fun setDriverId(context: Context, value: String?) {
            val editor: SharedPreferences.Editor = context.getSharedPreferences("123", Context.MODE_PRIVATE).edit()
            editor.putString("driver-id", value)
            editor.apply()
        }

        inline fun <reified T>getData(context: Context, key: String): T? {
            return Gson().fromJson(context.getSharedPreferences("123", Context.MODE_PRIVATE).getString(key, null), T::class.java)
        }

        fun <T>setData(context: Context, key: String, data: T) {
            val editor: SharedPreferences.Editor = context.getSharedPreferences("123", Context.MODE_PRIVATE).edit()
            editor.putString(key, Gson().toJson(data))
            editor.apply()
        }
    }

}