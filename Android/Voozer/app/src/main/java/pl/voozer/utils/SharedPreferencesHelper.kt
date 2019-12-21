package pl.voozer.utils

import android.content.Context
import android.content.SharedPreferences
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
    }

}