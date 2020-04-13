package com.sp62b21.myapp.service.alarm

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.sp62b21.myapp.data.models.Product
import android.content.Context.NOTIFICATION_SERVICE
import android.util.Log
import com.sp62b21.myapp.ui.view.settings.FragmentSettings.Companion.mCalendar
import com.sp62b21.myapp.utils.PreferenceHelper
import com.sp62b21.myapp.utils.PreferenceHelper.Companion.HOUR_SET
import com.sp62b21.myapp.utils.PreferenceHelper.Companion.MINUTE_SET
import java.util.*

class AlarmHelper private constructor() {
    private lateinit var mAlarmManager: AlarmManager
    private lateinit var mContext: Context

    fun init(context: Context) {
        this.mContext = context
        mAlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    }

    fun setAlarm(product: Product) {
        val preferenceHelper = PreferenceHelper.getInstance()
        val intent = Intent(mContext, AlarmReceiver::class.java)
            .putExtra("title", product.title)
            .putExtra("time_stamp", product.timeStamp)
        val pendingIntent = PendingIntent.getBroadcast(mContext, product.timeStamp.toInt(),
                intent, PendingIntent.FLAG_UPDATE_CURRENT)
        mCalendar = Calendar.getInstance()
        mCalendar.timeInMillis = product.expiredDate
        mCalendar.apply {
            set(Calendar.HOUR_OF_DAY, preferenceHelper.getInt(HOUR_SET))
            set(Calendar.MINUTE, preferenceHelper.getInt(MINUTE_SET))
            set(Calendar.SECOND, 0)
        }
        Log.i("SetAlarm","HOUR_SET: ${preferenceHelper.getInt(HOUR_SET)}")
        Log.i("SetAlarm","MINUTE_SET: ${preferenceHelper.getInt(MINUTE_SET)}")
        Log.i("SetAlarm","mCalendar: ${product.title+"_"+mCalendar.timeInMillis}")
        mAlarmManager.set(AlarmManager.RTC_WAKEUP, mCalendar.timeInMillis, pendingIntent)
    }

    fun removeNotification(productTimeStamp: Long, context: Context) {
        val notificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(productTimeStamp.toInt())
    }

    fun removeAlarm(productTimeStamp: Long) {
        val intent = Intent(mContext, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(mContext, productTimeStamp.toInt(),
                intent, PendingIntent.FLAG_UPDATE_CURRENT)
        mAlarmManager.cancel(pendingIntent)
    }

    companion object {
        private var mInstance: AlarmHelper? = null

        fun getInstance(): AlarmHelper {
            if (mInstance == null) {
                mInstance =
                    AlarmHelper()
            }
            return mInstance as AlarmHelper
        }
    }
}
