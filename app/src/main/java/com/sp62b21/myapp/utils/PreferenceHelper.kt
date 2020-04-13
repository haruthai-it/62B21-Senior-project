package com.sp62b21.myapp.utils

import android.content.Context
import android.content.SharedPreferences

class PreferenceHelper private constructor() {
    private lateinit var mPreferences: SharedPreferences

    fun init(context: Context) {
        mPreferences = context.getSharedPreferences("preferences", Context.MODE_PRIVATE)
    }

    fun putBoolean(key: String, value: Boolean) {
        mPreferences.edit().apply {
            putBoolean(key, value)
            apply()
        }
    }

    fun getBoolean(key: String) = mPreferences.getBoolean(key, true)

    fun getInt(key: String) = mPreferences.getInt(key, 0)

    fun putInt(key: String, value: Int) {
        mPreferences.edit().apply {
            putInt(key, value)
            apply()
        }
    }

    companion object {
        const val HOUR_SET = "hour_set"
        const val MINUTE_SET = "minute_set"
        const val IS_NOTIFICATION_TIME_CHANGED = "is_notification_time_changed"

        private var mInstance: PreferenceHelper? = null

        fun getInstance(): PreferenceHelper {
            if (mInstance == null) {
                mInstance = PreferenceHelper()
            }
            return mInstance as PreferenceHelper
        }
    }
}
