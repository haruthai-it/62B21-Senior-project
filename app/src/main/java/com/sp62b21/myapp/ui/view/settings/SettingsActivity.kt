package com.sp62b21.myapp.ui.view.settings

import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.MenuItem
import android.view.WindowManager
import com.sp62b21.myapp.R
import com.sp62b21.myapp.ui.view.base.BaseActivity
import kotlinx.android.synthetic.main.toolbar.*

class SettingsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        initToolbar(getString(R.string.settings))
        openSettingsFragment()
    }

    fun setToolbarTitle(title: String) {
        tvToolbarTitle.text = title
        checkScreenResolution()
    }

    private fun checkScreenResolution() {
        val displayMetrics = DisplayMetrics()
        (this.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.getMetrics(displayMetrics)
        val width = displayMetrics.widthPixels
        val height = displayMetrics.heightPixels

        if (width <= 480 || height <= 800) {
            tvToolbarTitle.textSize = 18F
        }
    }

    private fun openSettingsFragment() {
            supportFragmentManager.beginTransaction().replace(R.id.flFragmentContainer, FragmentSettings()).commit()
    }

    override fun onResume() {
        super.onResume()
        setToolbarTitle(getString(R.string.settings))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return false
    }

    override fun onPause() {
        super.onPause()

//        val intent = Intent(this, WidgetProvider::class.java)
//        intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
//        val ids = AppWidgetManager.getInstance(this)
//                .getAppWidgetIds(ComponentName(this, WidgetProvider::class.java))
//        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
//        sendBroadcast(intent)
    }
}
