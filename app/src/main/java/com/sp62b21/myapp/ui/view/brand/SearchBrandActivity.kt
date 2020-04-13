package com.sp62b21.myapp.ui.view.brand

import android.content.Intent
import android.graphics.Canvas
import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.sp62b21.myapp.R
import com.sp62b21.myapp.data.models.Brand
import com.sp62b21.myapp.ui.dialogs.AddBrandDialogFragment
import com.sp62b21.myapp.ui.recycler.RecyclerBrandViewAdapter
import com.sp62b21.myapp.ui.view.base.BaseActivity
import com.sp62b21.myapp.ui.view.base.BaseProductActivity
import com.sp62b21.myapp.utils.gone
import com.sp62b21.myapp.utils.visible
import com.sp62b21.myapp.vm.SearchBrandsViewModel
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import kotlinx.android.synthetic.main.activity_search.*
import kotterknife.bindView


class SearchBrandActivity : BaseActivity(), SearchView.OnQueryTextListener {
    private var mBrandList = arrayListOf<Brand>()
    private val mRecyclerView: RecyclerView by bindView(R.id.rvSearchResultsList)
    val mBrandText: TextView by bindView(R.id.tvBrandName)
    private lateinit var mViewModel: SearchBrandsViewModel
    private lateinit var mAdapter: RecyclerBrandViewAdapter
    private var mSnackbar: Snackbar? = null

    internal enum class ButtonsState {
        GONE, LEFT_VISIBLE, RIGHT_VISIBLE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        initToolbar(getString(R.string.search_brand))

        createBrandTouchHelper()

        mViewModel = createViewModel()
        mViewModel.liveData.observe(this, Observer<List<Brand>> { response -> updateViewState(response) })
        mViewModel.searchResultLiveData.observe(this, Observer<List<Brand>> { response -> updateViewState(response) })

        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mAdapter = RecyclerBrandViewAdapter()
        mRecyclerView.adapter = mAdapter

        mAdapter.setOnBrandClickListener(object : RecyclerBrandViewAdapter.ClickListener {
            override fun onBrandClick(v: View, position: Int) {
                val brand = mAdapter.getBrandAtPosition(position)
                setResult(BaseProductActivity.BRAND_INPUT_CODE, Intent().putExtra("brand", brand))
                finish()
//                saveBrand()
            }
        })
        restoreData()
    }


    private fun restoreData() {
//        val brand: Brand? = intent.getParcelableExtra("brand")
//        if (brand != null) {
//            tvProductBrandText.apply {
//                setText(brand.name)
//            }
//        }
    }

    private fun updateViewState(brands: List<Brand>) = if (brands.isEmpty()) showEmptyView(false)
        else showBrandList(brands)

    private fun showEmptyView(isSearchFieldEmpty: Boolean) {
        mAdapter.updateData(arrayListOf())
        llEmptyView.visible()
        if (isSearchFieldEmpty) {
            ivEmptyIllustration.setImageDrawable(AppCompatResources.getDrawable(this,
                    R.drawable.empty))
//            tvEmptyTitle.text = getString(R.string.search_view_empty_text)
        } else {
            tvEmptyTitle.text = getString(R.string.search_view_not_found_text)
        }
    }

    private fun showBrandList(brands: List<Brand>) {
        llEmptyView.gone()
        mAdapter.updateData(brands)
    }

    private fun createViewModel() = ViewModelProviders.of(this).get(
        SearchBrandsViewModel(
            application
        )::class.java)

    private fun deleteBrand(position: Int) {
        val deletedBrand = mAdapter.getBrandAtPosition(position)
        mAdapter.removeBrand(position)
        mViewModel.deleteBrand(deletedBrand)

        mSnackbar = Snackbar.make(mRecyclerView, R.string.snackbar_remove_brand, Snackbar.LENGTH_LONG)
        mSnackbar?.setAction(R.string.snackbar_undo) {
            mViewModel.saveBrand(deletedBrand)

            Handler().postDelayed({
                val firstCompletelyVisibleBrand = (mRecyclerView.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
                if (firstCompletelyVisibleBrand != 0 ) {
                    setToolbarShadow(0f, 10f)
                }
            }, 100)
        }

        mSnackbar?.view?.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(view: View) {
                val firstCompletelyVisibleBrand = (mRecyclerView.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
                val lastCompletelyVisibleBrand = (mRecyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()

                if (firstCompletelyVisibleBrand == 0 && lastCompletelyVisibleBrand == mBrandList.size - 1 ) {
                    setToolbarShadow(10f, 0f)
                }
            }

            override fun onViewDetachedFromWindow(view: View) {

            }

        })
        mSnackbar?.show()
    }
    
    private fun createBrandTouchHelper() {
        val helper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT ) {

//
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
//                moveTask(viewHolder.adapterPosition, target.adapterPosition)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                deleteBrand(viewHolder.adapterPosition)
            }

            override fun onChildDraw( c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean ) {
                RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addSwipeLeftBackgroundColor(ContextCompat.getColor(this@SearchBrandActivity, R.color.red))
                        .addSwipeLeftActionIcon(R.drawable.round_delete_outline_black_24)
                        .setActionIconTint(ContextCompat.getColor(this@SearchBrandActivity, R.color.white))
                            .create()
                            .decorate();
                super.onChildDraw( c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }



        })
        helper.attachToRecyclerView(mRecyclerView)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.searchwithmenu, menu)
        val searchView = menu.findItem(R.id.search).actionView as SearchView
//        searchView.isIconified = false
        val icon: ImageView = searchView.findViewById(androidx.appcompat.R.id.search_button)
        icon.setColorFilter(ContextCompat.getColor(this, R.color.black))

        val close: ImageView = searchView.findViewById(androidx.appcompat.R.id.search_close_btn)
        close.setColorFilter(ContextCompat.getColor(this, R.color.black))
        val searchText: TextView = searchView.findViewById(androidx.appcompat.R.id.search_src_text)
        close.setOnClickListener {
            searchText.text = ""
            searchView.onActionViewCollapsed()
        }
        searchView.setOnQueryTextListener(this)
        searchView.queryHint = getString(R.string.search)
        searchText.setBackgroundResource(R.drawable.search_view_background)
        val view: View = searchView.findViewById(androidx.appcompat.R.id.search_plate)
        searchText.setHintTextColor(ContextCompat.getColor(this, R.color.grey))
        view.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        when (item.itemId) {
            R.id.menu_add -> {
                showCreateCategoryDialog()

            }
        }

        return false
    }

    fun showCreateCategoryDialog() {
        val context = this
        val builder = AlertDialog.Builder(context, R.style.AlertDialogStyle_Light)
        val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT)
        params.setMargins(16, 16, 16, 16)

        val title = TextView(this)
        title.setText(getString(R.string.dialog_brand_message))
        title.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
        title.setPadding(16, 16, 16, 16)
        title.setGravity(Gravity.CENTER)
        title.setTextColor(ContextCompat.getColor(this, R.color.darkGrey))
        title.setTextSize(20F)
        title.layoutParams = params


        builder.setCustomTitle(title)

        // https://stackoverflow.com/questions/10695103/creating-custom-alertdialog-what-is-the-root-view
        // Seems ok to inflate view with null rootView
        val mView = layoutInflater.inflate(R.layout.dialog_addbrand, null)

        val mBrandEditText = mView.findViewById(R.id.brandEditText) as EditText

        builder.setView(mView);

        showKeyboard(mBrandEditText)

        // set up the ok button
        builder.setPositiveButton(android.R.string.ok) { dialog, p1 ->
            val newBrand = mBrandEditText.text
            var isValid = true
            if (newBrand.isBlank()) {
                mBrandEditText.error = getString(R.string.error_text_input)
                isValid = false
            }

            if (isValid) {
                val brand = Brand().apply {
                    name = mBrandEditText.text.toString()
                }
                mViewModel.saveBrand(brand)
                setResult(BaseProductActivity.BRAND_INPUT_CODE, Intent().putExtra("brand", brand))
                finish()
            }

            if (isValid) {
                dialog.dismiss()
            }
        }

        builder.setNegativeButton(android.R.string.cancel) { dialog, p1 ->
            dialog.cancel()
        }

        builder.show();

        mViewModel.liveData.observe(this, Observer<List<Brand>> { response -> updateViewState(response) })

//        val parent = positiveButton.getParent() as LinearLayout
//        parent.gravity = Gravity.CENTER_HORIZONTAL
//        val leftSpacer = parent.getChildAt(1)
//        leftSpacer.visibility = View.GONE
//        showAddBrandDialog()
//        setResult(Activity.RESULT_OK, Intent().putExtra("brand", brand))
//                finish()
    }

    private fun showAddBrandDialog() = AddBrandDialogFragment().show(supportFragmentManager, null)

    override fun onQueryTextSubmit(query: String?) = false

    override fun onQueryTextChange(newText: String): Boolean {
        if (newText.isEmpty()) {
            mViewModel.liveData.observe(this, Observer<List<Brand>> { response -> updateViewState(response) })
        } else mViewModel.searchInputLiveData.value = newText
        return true
    }

    companion object {
        const val BRAND_INPUT_CODE = 444
    }


}
