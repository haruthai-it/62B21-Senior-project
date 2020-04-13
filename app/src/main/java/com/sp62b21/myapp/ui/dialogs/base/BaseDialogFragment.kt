package com.sp62b21.myapp.ui.dialogs.base

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.sp62b21.myapp.R

abstract class BaseDialogFragment : DialogFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.apply {
            requestFeature(Window.FEATURE_NO_TITLE)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(resources.getDimensionPixelSize(R.dimen.dialog_width), ViewGroup.LayoutParams.WRAP_CONTENT)
    }

}