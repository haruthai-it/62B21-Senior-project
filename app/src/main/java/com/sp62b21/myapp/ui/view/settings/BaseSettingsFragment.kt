package com.sp62b21.myapp.ui.view.settings

import androidx.fragment.app.Fragment
import com.sp62b21.myapp.R

abstract class BaseSettingsFragment : Fragment() {

    fun setTitle(title: String) {
        (activity as SettingsActivity).setToolbarTitle(title)
    }

    override fun onDetach() {
        super.onDetach()
        setTitle(getString(R.string.settings))
    }
}