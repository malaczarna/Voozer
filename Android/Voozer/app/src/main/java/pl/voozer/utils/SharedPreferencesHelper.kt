package pl.voozer.utils

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesHelper {
    companion object {

        fun getToken(context: Context): String? {
            return context.getSharedPreferences("123", Context.MODE_PRIVATE).getString("token", "")
        }
    }

}