package com.sp62b21.myapp.ui.dialogs

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.sp62b21.myapp.R
import com.sp62b21.myapp.ui.dialogs.base.BaseDialogFragment
import com.sp62b21.myapp.utils.toast
import com.sp62b21.myapp.vm.RestoreBackupViewModel
import kotlinx.android.synthetic.main.dialog_default.*

class RestoreBackupDialogFragment : BaseDialogFragment() {
    private lateinit var mViewModel: RestoreBackupViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.dialog_default, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let { mViewModel = createViewModel(it.application) }
        initDialog()
    }

    private fun initDialog() {
        tvDialogMessage.setText(R.string.backup_restore_dialog_message)
        tvConfirm.setText(R.string.backup_restore_dialog_button)
        tvConfirm.setOnClickListener { restoreBackup() }
        tvCancel.setOnClickListener { dismiss() }
    }

    private fun restoreBackup() {
        if (mViewModel.isBackupExist()) {
            mViewModel.restoreBackup()

            if (mViewModel.isBackupRestoredSuccessfully()) {
                dismiss()
                toast(getString(R.string.backup_restore_message_success))
            } else {
                dismiss()
                toast(getString(R.string.backup_restore_message_failure))
            }
        } else {
            dismiss()
            toast(getString(R.string.backup_restore_message_nothing))
        }
    }

    private fun createViewModel(application: Application) = ViewModelProviders.of(this).get(
        RestoreBackupViewModel(application)::class.java)
}