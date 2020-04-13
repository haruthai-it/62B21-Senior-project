package com.sp62b21.myapp.ui.view.base

import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ScrollView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.toolbar.*
import androidx.appcompat.app.AppCompatActivity
import com.sp62b21.myapp.R
import com.sp62b21.myapp.data.database.PAODao
import com.sp62b21.myapp.data.database.AppDatabase
import com.sp62b21.myapp.data.models.Product
import com.sp62b21.myapp.ui.view.product.EditProductActivity

abstract class BaseActivity : AppCompatActivity() {
    private var db: AppDatabase? = null
    private var paoDao: PAODao? = null

    override fun onResume() {
        super.onResume()
        initStatusBar()
    }

//    fun initDB() {
//            Observable.fromCallable({
//                db = AppDatabase.getAppDataBase(context = this)
//                paoDao = db?.paoDao()
//
//                var gender1 = PAO("Male")
//                var gender2 = PAO("Female")
//
//                with(PAODao){
//                    this?.insertPAO(gender1)
//                    this?.insertPAO(gender2)
//                }
//                PAODao()?.getGenders()
//            }).doOnNext({ list ->
//                var finalString = ""
//                list?.map { finalString+= it.name+" - " }
//                tv_message.text = finalString
//
//            }).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe()
//
//        }

    fun initToolbar(titleText: String = "", drawable: Int? = R.drawable.round_arrow_back_black_24, view: Toolbar? = toolbar) {
        title = ""
        tvToolbarTitle.text = titleText

        if (toolbar != null) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor = ContextCompat.getColor(this, R.color.greyWhite)
            }
            setSupportActionBar(view)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            if (drawable != null) {
                supportActionBar?.setHomeAsUpIndicator(drawable)
            }
        }

    private fun initStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    var flags = toolbar.systemUiVisibility
                    flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                    toolbar.systemUiVisibility = flags
                    this.window.statusBarColor = Color.WHITE
        }
    }

    private fun openApplicationSettings() =
        startActivityForResult(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.parse("package:$packageName")), PERMISSION_REQUEST_CODE)

    fun requestPerms(permission: String, fragment: Fragment? = null) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (fragment != null) {
                fragment.requestPermissions(arrayOf(permission), PERMISSION_REQUEST_CODE)
            } else requestPermissions(arrayOf(permission), PERMISSION_REQUEST_CODE)
        }
    }
    
    fun showProductDetailsActivity(product: Product) {
        val intent = Intent(this, EditProductActivity::class.java).apply {
            putExtra("id", product.id)
            putExtra("title", product.title)
            putExtra("brand", product.brand)
            putExtra("category", product.category)
            putExtra("openedDate", product.openedDate)
            putExtra("pao", product.pao)
            putExtra("bb", product.bb)
            putExtra("expiredDate", product.expiredDate)
            putExtra("note", product.note)
            putExtra("image", product.image)
            putExtra("time_stamp", product.timeStamp)
//            if (product.date != 0L) {
//                putExtra("date", product.date)
//            }
        }
        startActivity(intent)
    }

    fun setToolbarShadow(start: Float, end: Float) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ValueAnimator.ofFloat(start, end).apply {
                addUpdateListener { updatedAnimation ->
                    toolbar.elevation = updatedAnimation.animatedValue as Float
                }
                duration = 500
                start()
            }
        }
    }

    fun initScrollViewListener(scrollView: ScrollView) {
        var isShadowShown = false

        scrollView.viewTreeObserver.addOnScrollChangedListener {
            if (scrollView.scrollY > 0 && !isShadowShown) {
                setToolbarShadow(0f, 10f)
                isShadowShown = true
            } else if (scrollView.scrollY == 0 && isShadowShown) {
                setToolbarShadow(10f, 0f)
                isShadowShown = false
            }
        }
    }

    fun isHasPermissions(permission: String): Boolean {
        var result: Int

        for (currentPermission in arrayOf(permission)) {
            result = checkCallingOrSelfPermission(currentPermission)
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    fun showNoPermissionSnackbar(view: View, snackbarMessage: String, toastMessage: String, anchorView: View? = null) {
        val snackbar = Snackbar.make(view, snackbarMessage, Snackbar.LENGTH_LONG)
            .setAction(R.string.permission_snackbar_button_settings) {
                openApplicationSettings()
                Toast.makeText(this, toastMessage, Toast.LENGTH_LONG).show()
            }
        if (anchorView != null) {
            snackbar.anchorView = anchorView
        }
        snackbar.show()
    }

    fun requestPermissionWithRationale(view: View, message: String, permission: String, callback: PermissionRequestListener? = null, anchorView: View? = null) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
            val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .setAction(R.string.permission_snackbar_button_grant) {
                    if (callback != null) {
                        callback.onPermissionRequest()
                    } else requestPerms(permission)
                }
            if (anchorView != null) {
                snackbar.anchorView = anchorView
            }
            snackbar.show()
        } else {
            if (callback != null) {
                callback.onPermissionRequest()
            } else requestPerms(permission)
        }
    }

    fun showKeyboard(editText: EditText) {
        editText.requestFocus()
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }

    fun hideKeyboard(editText: EditText) {
        editText.clearFocus()
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(editText.windowToken, 0)
    }

    interface PermissionRequestListener {
        fun onPermissionRequest()
    }

    companion object {
        const val PERMISSION_REQUEST_CODE = 123
    }

}