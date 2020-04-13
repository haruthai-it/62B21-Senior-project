package com.sp62b21.myapp.ui.dialogs

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.sp62b21.myapp.R
import com.sp62b21.myapp.data.models.Product
import com.sp62b21.myapp.service.alarm.AlarmHelper
import com.sp62b21.myapp.ui.dialogs.base.BaseDialogFragment
import com.sp62b21.myapp.vm.product.DeleteProductViewModel
import kotlinx.android.synthetic.main.dialog_default.*

class DeleteProductDialogFragment(val product: Product) : BaseDialogFragment() {
    private lateinit var mViewModel: DeleteProductViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.dialog_default, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let { mViewModel = createViewModel(it.application) }
        initDialog()
    }

    private fun initDialog() {
        tvDialogMessage.setText(R.string.dialog_message)
        tvConfirm.setText(R.string.action_delete)
        tvConfirm.setOnClickListener {
            mViewModel.deleteProduct(product)
            if (product.expiredDate != 0L) {
                val alarmHelper = AlarmHelper.getInstance()
                alarmHelper.removeAlarm(product.timeStamp)
                alarmHelper.removeNotification(product.timeStamp, activity!!.applicationContext)
            }
            activity?.finish()
        }
        tvCancel.setOnClickListener { dismiss() }
    }

    private fun createViewModel(application: Application) = ViewModelProviders.of(this).get(
        DeleteProductViewModel(application)::class.java)
}
