package com.sp62b21.myapp.ui.view.base

import android.app.Activity
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.sp62b21.myapp.R
import com.sp62b21.myapp.data.models.Brand
import com.sp62b21.myapp.data.models.PAO
import com.sp62b21.myapp.ui.recycler.PAOAdapter
import com.sp62b21.myapp.ui.view.brand.SearchBrandActivity
import com.sp62b21.myapp.utils.*
import com.sp62b21.myapp.vm.PaoViewModel
import com.sp62b21.myapp.vm.base.BaseViewModel
import kotlinx.android.synthetic.main.activity_product_details.*
import kotterknife.bindView
import java.util.*


abstract class BaseProductActivity : BaseActivity() {
    val mTitleEditText: EditText by bindView(R.id.etProductTitle)
    val mNoteEditText: EditText by bindView(R.id.etProductNote)
    var imgPath: String? = null
    var selectedPao: PAO? = null
    var mOpenedDate: Long = 0
    var mBb: Long = 0
    var mExpiredDate: Long = 0

    val mGridPAO: RecyclerView by bindView(R.id.rvProductPAO)
    var mPAOList = ArrayList<PAO>()
    lateinit var mPAOadapter: PAOAdapter
    lateinit var mOpenedCalendar: Calendar
    lateinit var mExpiredCalendar: Calendar
    lateinit var mBBCalendar: Calendar
    private lateinit var mViewModel: PaoViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)

        //Get Intent
        mOpenedDate = intent.getLongExtra("openedDate", 0)
        mBb = intent.getLongExtra("bb", 0)
        mExpiredDate = intent.getLongExtra("expiredDate", 0)

        mOpenedCalendar = Calendar.getInstance()
        if(mOpenedDate == 0L){
            mOpenedCalendar = Calendar.getInstance()
        } else mOpenedCalendar.timeInMillis = mOpenedDate

        tvProductOpenedDate.visible()
        tvProductOpenedDate.text = DateAndTime.getFullDate(mOpenedCalendar.timeInMillis)
        ivProductOpenedIcon.gone()
        ivDeleteProductOpened.visible()

        mBBCalendar = Calendar.getInstance()
        if(mBb == 0L){
            mBBCalendar.timeInMillis = 0
        } else mBBCalendar.timeInMillis = mBb

        mExpiredCalendar = Calendar.getInstance()
        if(mExpiredDate == 0L){
            mExpiredCalendar.timeInMillis = 0
        } else mExpiredCalendar.timeInMillis = mExpiredDate


        mViewModel = createPAOViewModel()
        mViewModel.mAllPAOsLiveData.observe(this, Observer<List<PAO>> { response -> updateViewState(response) })

        mGridPAO.layoutManager = GridLayoutManager(this, 5)
        mPAOadapter = PAOAdapter()
        mGridPAO.adapter = mPAOadapter

        initListeners()
    }



    private fun initListeners() {
        mTitleEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                if (mTitleEditText.length() != 0) {
                    tilProductTitle.error = null
                    tilProductTitle.endIconDrawable = null
                }
            }
        })

        imageProduct.setOnClickListener {
            showImgPicker()
        }


        mPAOadapter.setOnPaoClickListener(object : PAOAdapter.ClickListener {
            override fun onPaoClick(v: View, position: Int) {
                mPAOadapter.notifyDataSetChanged()
                val selectedPao = mPAOadapter.getPAOAtPosition(position)
                intent.putExtra("pao", selectedPao)
                val paoText: String = selectedPao.month
                val month: Int?
                mExpiredCalendar.timeInMillis = 0
                when{ mBBCalendar.timeInMillis == 0L ->
                if (paoText!="∞"){
                    month = paoText.substringBefore("M").toInt()
                    mExpiredCalendar.apply {
                        set(Calendar.YEAR, mOpenedCalendar.get(Calendar.YEAR))
                        set(Calendar.MONTH, mOpenedCalendar.get(Calendar.MONTH))
                        set(Calendar.DAY_OF_MONTH, mOpenedCalendar.get(Calendar.DAY_OF_MONTH))
                    }
                    mExpiredCalendar.add(Calendar.MONTH, month)
                    tvProductExpiredDate.setText(DateAndTime.getFullDate(mExpiredCalendar.timeInMillis))
                } else {
                    mExpiredCalendar.timeInMillis = 0
                    tvProductExpiredDate.text = null
                }
                else -> {mExpiredCalendar.timeInMillis = mBBCalendar.timeInMillis
                       tvProductExpiredDate.text = DateAndTime.getFullDate(mExpiredCalendar.timeInMillis)}
                }
                Log.i("BaseProduct","On PAO Clicked BB: ${mBBCalendar.timeInMillis}")
                Log.i("BaseProduct","On PAO Clicked Expired: ${mExpiredCalendar.timeInMillis}")
            }
        })


        rlProductBrand.setOnClickListener {
            hideKeyboard(mTitleEditText)
            val selectedBrand: Brand? = intent.getParcelableExtra("brand")
            startActivityForResult(Intent(this, SearchBrandActivity::class.java).putExtra("brand",selectedBrand), BRAND_INPUT_CODE)


        }

        rlProductOpened.setOnClickListener {
            hideKeyboard(mTitleEditText)
            val dateSetListener = object : OnDateSetListener {
                override fun onDateSet(datePicker: DatePicker, year: Int, monthOfYear: Int,
                                       dayOfMonth: Int) {
                    mOpenedCalendar.apply {
                        set(Calendar.YEAR, year)
                        set(Calendar.MONTH, monthOfYear)
                        set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    }
                    tvProductOpenedDate.visible()
                    tvProductOpenedDate.text = DateAndTime.getFullDate(mOpenedCalendar.timeInMillis)
                    ivProductOpenedIcon.gone()
                    ivDeleteProductOpened.visible()
                }
            }
            showDatePickerDialog(dateSetListener)
        }


        rlProductBb.setOnClickListener {
            hideKeyboard(mTitleEditText)
            val dateSetListener = object : OnDateSetListener {
                override fun onDateSet(datePicker: DatePicker, year: Int, monthOfYear: Int,
                                       dayOfMonth: Int) {
                    mBBCalendar.apply {
                        set(Calendar.YEAR, year)
                        set(Calendar.MONTH, monthOfYear)
                        set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    }

                    when {
                        mBBCalendar.timeInMillis < mOpenedCalendar.timeInMillis -> toast(getString(R.string.toast_incorrect_date))
                        else -> {
                            tvProductBBDate.visible()
                            tvProductBBDate.text = DateAndTime.getFullDate(mBBCalendar.timeInMillis)
                            ivProductBBIcon.gone()
                            ivDeleteProductBB.visible()
                            mExpiredCalendar = mBBCalendar
                            tvProductExpiredDate.setText(DateAndTime.getFullDate(mExpiredCalendar.timeInMillis))
                        }
                    }

                }
            }
            showDatePickerDialog(dateSetListener)
        }

        ivDeleteProductOpened.setOnClickListener {
            tvProductOpenedDate.text = null
            ivDeleteProductOpened.gone()
            ivProductOpenedIcon.visible()
            tvProductOpenedDate.invisible()
        }

        ivDeleteProductBB.setOnClickListener {
            mBBCalendar.timeInMillis = 0
            mExpiredCalendar.timeInMillis = 0
            tvProductExpiredDate.text = null
            tvProductBBDate.text = null
            ivDeleteProductBB.gone()
            ivProductBBIcon.visible()
            tvProductBBDate.invisible()
            Log.i("BaseProduct","On Delete BB: ${mBBCalendar.timeInMillis}")
            Log.i("BaseProduct","On Delete Expired: ${mExpiredCalendar.timeInMillis}")
        }

        initScrollViewListener(svProductDetails)
    }

    private fun showDatePickerDialog(dateSetListener: OnDateSetListener) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerStyle = R.style.DatePicker_Light

        DatePickerDialog(this, datePickerStyle, dateSetListener, year, month, day).apply {
            show()
            window?.setLayout(
                resources.getDimensionPixelSize(R.dimen.dialog_width),
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(
                ContextCompat.getColor(
                    this@BaseProductActivity,
                    R.color.blue
                )
            )
            getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(
                ContextCompat.getColor(
                    this@BaseProductActivity,
                    R.color.blue
                )
            )
            getButton(DatePickerDialog.BUTTON_POSITIVE).setBackgroundColor(Color.TRANSPARENT)
            getButton(DatePickerDialog.BUTTON_NEGATIVE).setBackgroundColor(Color.TRANSPARENT)
        }
    }

    private fun updateViewState(paos: List<PAO>) =  showPaoList(paos)

    private fun showPaoList(paos: List<PAO>) {
        mPAOadapter.updateData(paos)
    }

    abstract fun createViewModel(): BaseViewModel

    fun createPAOViewModel() = ViewModelProviders.of(this).get(
        PaoViewModel(
            application
        )::class.java)

    override fun onStop() {
        super.onStop()
        hideKeyboard(mTitleEditText)
    }

    private fun showImgPicker() {
        ImagePicker.with(this)
            .crop(1f, 1f)
            .compress(1024)
            .maxResultSize(1080, 1080)
            .start()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == BRAND_INPUT_CODE) {
            val selectedBrand: Brand? = data?.getParcelableExtra("brand")
            if (selectedBrand != null) {
                tvNotificationTimeText.text = selectedBrand.name
                tvNotificationTimeText.visible()
            }
            intent.putExtra("brand",selectedBrand?.name)
        }
        if (resultCode == Activity.RESULT_OK){
            //Image Uri will not be null for RESULT_OK
            imageProduct.imageTintList = null
            val fileUri = data?.data
            imgPath = fileUri.toString()
            Glide
                .with(this)
                .load(imgPath)
                .centerCrop()
                .placeholder(R.drawable.add_image)
                .into(imageProduct)
            Log.i("BaseProductActivity","$imgPath")
        }
    }


    override fun onOptionsItemSelected(item: MenuItem) =
        if (item.itemId == android.R.id.home) {
            hideKeyboard(mTitleEditText)
            onBackPressed()
            true
        } else false

    companion object {
        private const val IMAGE_INPUT_CODE = 333
        const val BRAND_INPUT_CODE = 444
        private const val APP_INTRO_CODE = 222
    }



    //    from_dateListener = new OnDateSetListener(){
//
//        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
//
//            ...
//
//        }
//    }
//
//};
//to_dateListener = new OnDateSetListener(){
//    public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
//        .....
//    }
//};
//    public var OpenedDateSetListener: OnDateSetListener? =
//        OnDateSetListener(datePicker, year, monthOfYear, dayOfMonth) {
//            mCalendar.apply {
//                set(Calendar.YEAR, year)
//                set(Calendar.MONTH, monthOfYear)
//                set(Calendar.DAY_OF_MONTH, dayOfMonth)
//            }
//            tvProductOpenedDate.visible()
//            tvProductOpenedDate.text =
//                getString(R.string.date_at, DateAndTime.getFullDate(mCalendar.timeInMillis))
//            ivProductOpenedIcon.gone()
//            ivDeleteProductOpened.visible()
//        }
//    }



//    override fun onDateSet(datePicker: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int) {
//        mCalendar.apply {
//            set(Calendar.YEAR, year)
//            set(Calendar.MONTH, monthOfYear)
//            set(Calendar.DAY_OF_MONTH, dayOfMonth)
//        }
//
//        tvProductOpenedDate.visible()
//        tvProductOpenedDate.text = DateAndTime.getFullDate(mCalendar.timeInMillis)
//        ivProductOpenedIcon.gone()
//        ivDeleteProductOpened.visible()
//    }
//
}