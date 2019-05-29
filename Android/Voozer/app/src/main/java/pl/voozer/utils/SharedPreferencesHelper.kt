package pl.voozer.utils

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesHelper {
    companion object {

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
    }

}