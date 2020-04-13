package com.sp62b21.myapp.ui.view.product

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.activity_product_details.*
import java.util.*
import com.sp62b21.myapp.utils.visible
import com.sp62b21.myapp.utils.toast
import com.sp62b21.myapp.R
import com.sp62b21.myapp.data.models.Brand
import com.sp62b21.myapp.data.models.PAO
import com.sp62b21.myapp.data.models.Product
import com.sp62b21.myapp.service.alarm.AlarmHelper
import com.sp62b21.myapp.ui.dialogs.DeleteProductDialogFragment
import com.sp62b21.myapp.vm.product.EditProductViewModel
import com.sp62b21.myapp.ui.view.base.BaseProductActivity
import com.sp62b21.myapp.utils.DateAndTime
import com.sp62b21.myapp.utils.gone


class EditProductActivity : BaseProductActivity() {
    private var mId: Long = 0
    private var mBrand: String? = null
    private var mCategory: Int? = 0
    private var mPao: Int? = 0
//    private var mPosition: Int = 0
    private var mTimeStamp: Long = 0
    private lateinit var mBrandList: List<Brand>
    private lateinit var mPAOsList: List<PAO>
//    private lateinit var mBrandList: LiveData<List<Brand>>
//    private val mSelectedBrand: Brand? = null
    private lateinit var mTitle: String
//    private lateinit var mBrand: String
    private lateinit var mSelectedPao: PAO
    private lateinit var mNote: String
    private var mImage: String? = null
    private lateinit var mViewModel: EditProductViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolbar(getString(R.string.edit_product))
        if (Locale.getDefault().displayLanguage == "français") {
            tvToolbarTitle.textSize = 18F
        }
        mViewModel = createViewModel()


        mBrandList = mViewModel.brandList
        mPAOsList = mViewModel.paoList

        // Get Intent data
        mId = intent.getLongExtra("id", 0)
        mTitle = intent.getStringExtra("title")
        mBrand = intent.getStringExtra("brand")
        mCategory = intent.getIntExtra("category", 0)
        mOpenedDate = intent.getLongExtra("openedDate", 0)
        mPao = intent.getIntExtra("pao", 0)
        mBb = intent.getLongExtra("bb", 0)
        mExpiredDate = intent.getLongExtra("expiredDate", 0)
        mNote = intent.getStringExtra("note")
        mImage = intent.getStringExtra("image")
        mTimeStamp = intent.getLongExtra("time_stamp", 0)



//        val mSelectedBrand: Brand? = mBrandList.find {it.id == 3 }

        val mSelectedPao: PAO? = mPAOsList.find { it.id == mPao }

        mTitleEditText.setText(mTitle)
//        mTitleEditText.setText(mSelectedBrand?.name)
        btnProductDelete.visible()

        if (mNote.isNotEmpty()) {
            mNoteEditText.setText(mNote)
//            tvProductNote.text = mNote
        }

        if (mImage != null) {
            imageProduct.imageTintList = null
            Glide
                .with(this)
                .load(mImage)
                .centerCrop()
                .placeholder(R.drawable.add_image)
                .into(imageProduct)
        }

        if (mBrand != null) {
            tvNotificationTimeText.text = mBrand
            tvNotificationTimeText.visible()
        }

        if (mExpiredDate != 0L) {
            tvProductExpiredDate.text = DateAndTime.getFullDate(mExpiredDate)
        }

        if (tvProductExpiredDate.length() != 0) {
            mExpiredCalendar.timeInMillis = mExpiredDate
        }

        if (mOpenedDate != 0L) {
            tvProductOpenedDate.text = DateAndTime.getFullDate(mOpenedDate)
            tvProductOpenedDate.visible()
            ivProductOpenedIcon.gone()
            ivDeleteProductOpened.visible()
        }

        if (mPao != null) {
            mPAOadapter.selectedPosition = mPao!!-1
        } else mPAOadapter.selectedPosition = -1

        if (tvProductOpenedDate.length() != 0) {
            mOpenedCalendar.timeInMillis = mOpenedDate
        }

        if (mBb != 0L) {
            tvProductBBDate.text = DateAndTime.getFullDate(mBb)
            tvProductBBDate.visible()
            ivProductBBIcon.gone()
            ivDeleteProductBB.visible()
        }

        if (tvProductBBDate.length() != 0) {
            mBBCalendar.timeInMillis = mBb
        }


        btnProductDelete.setOnClickListener {
            hideKeyboard(mTitleEditText)
            showDeleteProductDialog(Product(mId, mTitle, mBrand, mCategory, mOpenedDate, mPao, mBb, mExpiredDate, mNote, mImage,  mTimeStamp))
        }
    }

    private fun showDeleteProductDialog(Product: Product) = DeleteProductDialogFragment(Product).show(supportFragmentManager, null)

    override fun createViewModel() = ViewModelProviders.of(this).get(
        EditProductViewModel(
            application
        )::class.java)

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.edit_product_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        selectedPao = intent.getParcelableExtra("pao")

        when (item.itemId) {

            android.R.id.home -> {
                hideKeyboard(mTitleEditText)
                onBackPressed()
            }

            R.id.action_update -> {
                when {
                    mTitleEditText.length() == 0 -> tilProductTitle.error = getString(R.string.error_text_input)
                    mTitleEditText.text.toString().trim { it <= ' ' }.isEmpty() -> tilProductTitle.error = getString(R.string.error_spaces)
                    tvProductOpenedDate.length() == 0 -> toast(getString(R.string.toast_no_date))
                    else -> {
//                        val product = Product().apply {
//                            title = mTitleEditText.text.toString()
//                        }
                        val product = Product(mId, mTitleEditText.text.toString(), mBrand, mCategory, mOpenedDate, mPao, mBb, mExpiredDate, tvProductNote.text.toString(), mImage,  mTimeStamp)
                        product.expiredDate = mExpiredCalendar.timeInMillis
                        product.bb = mBBCalendar.timeInMillis

                        if (tvNotificationTimeText.length() != 0) {
                            product.brand = tvNotificationTimeText.text.toString()
                        }

                        if( imgPath != null){
                            product.image = imgPath
                        }

                        if( selectedPao != null){
                            product.pao = selectedPao?.id
                        }

                        if (tvProductBrand.length() != 0) {
                            product.note = tvProductBrand.text.toString()
                        }

                        if (tvProductOpenedDate.length() != 0) {
                            product.openedDate = mOpenedCalendar.timeInMillis
                        }

                        if (product.expiredDate != 0L) {
                            AlarmHelper.getInstance().setAlarm(product)
                        } else if (product.expiredDate == 0L) {
                            AlarmHelper.getInstance().apply {
                                removeAlarm(product.timeStamp)
                                removeNotification(product.timeStamp, this@EditProductActivity)
                            }
                        }

                        if (mNoteEditText.length() != 0) {
                            product.note = mNoteEditText.text.toString()
                        }
                        Log.i("EditProduct","tvProductBBDate: ${tvProductBBDate.length()}")
                        Log.i("EditProduct","BB: ${mBBCalendar.timeInMillis}")
                        Log.i("EditProduct","Expired: ${mExpiredCalendar.timeInMillis}")
                        Log.i("EditProduct","$product")
                        mViewModel.updateProduct(product)
                        finish()
                    }
                }
                hideKeyboard(mTitleEditText)
            }
        }
        return super.onOptionsItemSelected(item)
    }

}