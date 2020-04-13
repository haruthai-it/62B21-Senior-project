package com.sp62b21.myapp.ui.view.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sp62b21.myapp.R
import kotlinx.android.synthetic.main.fragment_licenses.*

class FragmentLicenses : BaseSettingsFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_licenses, container, false)
    }

    override fun onResume() {
        super.onResume()
        setTitle(getString(R.string.settings_page_title_licenses))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initButtons()
    }

    private fun initButtons() {
        clSimpleTodo.setOnClickListener { openUri(SIMPLETODO_PAGE) }
        clKotterKnife.setOnClickListener { openUri(KOTTER_KNIFE_PAGE) }
        clRxJava.setOnClickListener { openUri(RX_JAVA_PAGE) }
        clRxKotlin.setOnClickListener { openUri(RX_KOTLIN_PAGE) }
        clGlide.setOnClickListener { openUri(GLIDE_PAGE) }
        clImagePicker.setOnClickListener { openUri(IMAGEPICKER_PAGE) }
        clFreepik.setOnClickListener { openUri(FREEPIK_PAGE) }
        clFlaticon.setOnClickListener { openUri(FLATICON_PAGE) }
        clRecyclerViewSwipeDecorator.setOnClickListener { openUri(RECYCLERVIEWSWIPEDECORATOR_PAGE) }
    }

    private fun openUri(uri: String) = startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(uri)))

    private companion object {
        private const val SIMPLETODO_PAGE = "https://github.com/Jizzu/SimpleToDo"
        private const val KOTTER_KNIFE_PAGE = "https://github.com/JakeWharton/kotterknife"
        private const val RX_JAVA_PAGE = "https://github.com/ReactiveX/RxJava"
        private const val RX_KOTLIN_PAGE = "https://github.com/ReactiveX/RxKotlin"
        private const val GLIDE_PAGE = "https://github.com/bumptech/glide"
        private const val IMAGEPICKER_PAGE = "https://github.com/Dhaval2404/ImagePicker"
        private const val FREEPIK_PAGE = "https://www.freepik.com/macrovector"
        private const val FLATICON_PAGE = "https://www.flaticon.com/authors/kiranshastry"
        private const val RECYCLERVIEWSWIPEDECORATOR_PAGE = "https://github.com/xabaras/RecyclerViewSwipeDecorator"

    }
}
