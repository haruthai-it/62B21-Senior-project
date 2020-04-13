package com.sp62b21.myapp.ui.view.product

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.ViewModelProviders
import com.sp62b21.myapp.R
import com.sp62b21.myapp.data.models.Brand
import com.sp62b21.myapp.data.models.Product
import com.sp62b21.myapp.service.alarm.AlarmHelper
import com.sp62b21.myapp.ui.view.base.BaseProductActivity
import com.sp62b21.myapp.utils.toast
import com.sp62b21.myapp.vm.product.AddProductViewModel
import kotlinx.android.synthetic.main.activity_product_details.*

class AddProductActivity : BaseProductActivity() {
    private var selectedBrand: Brand? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initToolbar(getString(R.string.create_product))
        showKeyboard(mTitleEditText)

    }

    override fun createViewModel() = ViewModelProviders.of(this).get(
        AddProductViewModel(
            application
        )::class.java)

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.add, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        selectedBrand = intent.getParcelableExtra("brand")
        selectedPao = intent.getParcelableExtra("pao")

        when (item.itemId) {

            android.R.id.home -> {
                hideKeyboard(mTitleEditText)
                onBackPressed()
            }

            R.id.action_add -> {
                when {
                    mTitleEditText.length() == 0 -> tilProductTitle.error = getString(R.string.error_text_input)
                    mTitleEditText.text.toString().trim { it <= ' ' }.isEmpty() -> tilProductTitle.error = getString(R.string.error_spaces)
                    tvProductOpenedDate.length() == 0 -> toast(getString(R.string.toast_no_date))
                else -> {
                    val product = Product().apply {
                        title = mTitleEditText.text.toString()
                        pao = selectedPao?.id
                        image = imgPath
                    }

                    if (tvNotificationTimeText.length() != 0) {
                        product.brand = tvNotificationTimeText.text.toString()
                    }

                    if (tvProductExpiredDate.length() != 0) {
                        product.expiredDate = mExpiredCalendar.timeInMillis
                    }

                    if (tvProductBrand.length() != 0) {
                        product.note = tvProductBrand.text.toString()
                    }

                    if (tvProductOpenedDate.length() != 0) {
                        product.openedDate = mOpenedCalendar.timeInMillis
                    }

                    if (tvProductBBDate.length() != 0) {
                        product.bb = mBBCalendar.timeInMillis
                    }

                    if (mNoteEditText.length() != 0) {
                        product.note = mNoteEditText.text.toString()
                    }

                    if (product.expiredDate != 0L) {
                        AlarmHelper.getInstance().setAlarm(product)
                    }

                    Log.i("AddProduct","Alarm : $AlarmHelper")

                    val viewModel = createViewModel()
                    viewModel.saveProduct(product)
                    finish()
                }
            }
            hideKeyboard(mTitleEditText)
            }
        }
        return super.onOptionsItemSelected(item)
    }

}