package com.sp62b21.myapp.ui.dialogs

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.lifecycle.ViewModelProviders
import com.sp62b21.myapp.R
import com.sp62b21.myapp.data.models.Brand
import com.sp62b21.myapp.ui.dialogs.base.BaseDialogFragment
import com.sp62b21.myapp.vm.SearchBrandsViewModel
import kotlinx.android.synthetic.main.dialog_default.*

class AddBrandDialogFragment() : BaseDialogFragment() {
    private lateinit var mViewModel: SearchBrandsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.dialog_brand, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let { mViewModel = createViewModel(it.application) }
        initDialog()
    }

    private fun initDialog() {
        tvDialogMessage.setText(R.string.dialog_brand_message)
        tvConfirm.setText(R.string.action_add)
        tvConfirm.setOnClickListener {
            val mView = layoutInflater.inflate(R.layout.dialog_brand, null)
            val mBrandEditText: EditText = mView.findViewById(R.id.brandEditText)

            val brand = Brand().apply {
                name = mBrandEditText.text.toString()
            }
            mViewModel.saveBrand(brand)

            activity?.finish()

        }
        tvCancel.setOnClickListener { dismiss() }
    }

    private fun createViewModel(application: Application) = ViewModelProviders.of(this).get(
        SearchBrandsViewModel(application)::class.java)

    
}
