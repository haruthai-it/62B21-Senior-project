package com.sp62b21.myapp.ui.view.settings

import android.app.DatePickerDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TimePicker
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.sp62b21.myapp.R
import com.sp62b21.myapp.service.alarm.AlarmReceiver
import com.sp62b21.myapp.utils.DateAndTime
import com.sp62b21.myapp.utils.toast
import com.sp62b21.myapp.utils.PreferenceHelper
import com.sp62b21.myapp.utils.PreferenceHelper.Companion.HOUR_SET
import com.sp62b21.myapp.utils.PreferenceHelper.Companion.IS_NOTIFICATION_TIME_CHANGED
import com.sp62b21.myapp.utils.PreferenceHelper.Companion.MINUTE_SET
import com.sp62b21.myapp.utils.visible
import kotlinx.android.synthetic.main.fragment_settings.*
import java.util.*

class FragmentSettings : Fragment() , TimePickerDialog.OnTimeSetListener  {
    private lateinit var mPreferenceHelper: PreferenceHelper

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPreferenceHelper = PreferenceHelper.getInstance()
        initButtons()
        initNotificationSoundButton()
        mCalendar.apply {
            set(Calendar.HOUR_OF_DAY, mPreferenceHelper.getInt(HOUR_SET))
            set(Calendar.MINUTE, mPreferenceHelper.getInt(MINUTE_SET))
            set(Calendar.SECOND, 0)
        }
        tvNotificationTimeText.text = DateAndTime.getTime(mCalendar.timeInMillis)
        tvNotificationTimeText.visible()
    }

    private fun initNotificationSoundButton() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val notificationChannel = notificationManager.getNotificationChannel(AlarmReceiver.NOTIFICATION_CHANNEL_ID)

            if (notificationChannel == null) {
                val channel = NotificationChannel(AlarmReceiver.NOTIFICATION_CHANNEL_ID, context?.getString(R.string.notification_channel), NotificationManager.IMPORTANCE_HIGH).apply {
                    enableLights(true)
                    lightColor = Color.BLUE
                    enableVibration(true)
                }
                notificationManager.createNotificationChannel(channel)
            }

            tvNotificationTitle.setOnClickListener {
                val intent = Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS)
                    .putExtra(Settings.EXTRA_APP_PACKAGE, context?.packageName)
                    .putExtra(Settings.EXTRA_CHANNEL_ID, AlarmReceiver.NOTIFICATION_CHANNEL_ID)
                startActivity(intent)
            }
        } else {
            llSettings.showDividers = LinearLayout.SHOW_DIVIDER_MIDDLE
            tvNotificationTitle.setTextColor(Color.TRANSPARENT)
//            tvNotificationSoundSummary.setTextColor(Color.TRANSPARENT)
        }
    }

    private fun initButtons() {
        rlNotificationTime.setOnClickListener { showTimePickerDialog() }
        tvLicenses.setOnClickListener { openFragment(FragmentLicenses()) }
    }

    private fun showTimePickerDialog() {
        val context = this.context!!
        lateinit var timePickerDialog: TimePickerDialog

        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE) + 1
        timePickerDialog = TimePickerDialog(context, this, hour, minute, true)
        timePickerDialog.apply {
            show()
            window?.setLayout(resources.getDimensionPixelSize(R.dimen.dialog_picker_width), ViewGroup.LayoutParams.WRAP_CONTENT)
            getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(context, R.color.blue))
            getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(context, R.color.blue))
            getButton(DatePickerDialog.BUTTON_POSITIVE).setBackgroundColor(Color.TRANSPARENT)
            getButton(DatePickerDialog.BUTTON_NEGATIVE).setBackgroundColor(Color.TRANSPARENT)
        }
    }

    override fun onTimeSet(timePicker: TimePicker, hourOfDay: Int, minute: Int) {
        mCalendar.apply {
            set(Calendar.HOUR_OF_DAY, hourOfDay)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
        }
        mPreferenceHelper.putInt(HOUR_SET,hourOfDay)
        mPreferenceHelper.putInt(MINUTE_SET,minute)
        Log.i("OnTimeSet","HOUR_SET: ${mPreferenceHelper.getInt(HOUR_SET)}")
        Log.i("OnTimeSet","MINUTE_SET: ${mPreferenceHelper.getInt(MINUTE_SET)}")
        mPreferenceHelper.putBoolean(IS_NOTIFICATION_TIME_CHANGED,true)
        tvNotificationTimeText.text = DateAndTime.getTime(mCalendar.timeInMillis)
    }

    private fun openFragment(fragment: BaseSettingsFragment) =
            fragmentManager?.beginTransaction()?.replace(R.id.flFragmentContainer, fragment)?.addToBackStack(null)?.commit()


    private fun openUri(uri: String) {
        try {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(uri)))
        } catch (exception: android.content.ActivityNotFoundException) {
            toast(getString(R.string.settings_no_browser_apps))
        }
    }

    interface NotificationClickListener {
        fun onNotificationStateChanged()
    }
    

    companion object {
        var callback: NotificationClickListener? = null
        var mCalendar: Calendar = Calendar.getInstance()

        const val GOOGLE_PLAY_PAGE = "https://play.google.com/store/apps/developer?id=Ilya+Ponomarenko"
        const val GIT_HUB_PAGE = "https://github.com/Jizzu/SimpleToDo"
        const val PRIVACY_POLICY_PAGE = "https://github.com/Jizzu/SimpleToDo/blob/master/PRIVACY_POLICY.md"
        const val APP_PAGE_SHORT_LINK = "market://details?id="
        const val APP_PAGE_LONG_LINK = "https://play.google.com/store/apps/details?id="
        const val SCHEME = "mailto"
    }

}