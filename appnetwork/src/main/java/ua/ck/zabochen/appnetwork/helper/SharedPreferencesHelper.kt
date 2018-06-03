package ua.ck.zabochen.appnetwork.helper

import android.content.Context
import android.content.SharedPreferences
import ua.ck.zabochen.appnetwork.utils.Constant

class SharedPreferencesHelper {

    companion object {

        private fun getSharedPreferences(context: Context): SharedPreferences {
            return context.getSharedPreferences(Constant.SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE)
        }

        fun setApiKey(context: Context, apiKey: String) {
            getSharedPreferences(context)
                    .edit()
                    .putString(Constant.SHARED_PREFERENCES_KEY_API_KEY, apiKey)
                    .apply()
        }

        fun getApiKey(context: Context): String {
            return getSharedPreferences(context)
                    .getString(Constant.SHARED_PREFERENCES_KEY_API_KEY, "")
        }

    }

}