package com.sp62b21.myapp.ui.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sp62b21.myapp.R
import com.sp62b21.myapp.data.models.Product
import com.sp62b21.myapp.service.alarm.AlarmHelper
import com.sp62b21.myapp.ui.recycler.RecyclerViewAdapter
import com.sp62b21.myapp.ui.view.base.BaseActivity
import com.sp62b21.myapp.utils.gone
import com.sp62b21.myapp.utils.visible
import com.sp62b21.myapp.vm.product.SearchProductsViewModel
import kotlinx.android.synthetic.main.activity_search.*
import kotterknife.bindView


class SearchActivity : BaseActivity(), SearchView.OnQueryTextListener {
    private val mRecyclerView: RecyclerView by bindView(R.id.rvSearchResultsList)
    private lateinit var mViewModel: SearchProductsViewModel
    private lateinit var mAdapter: RecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        initToolbar()

        if (intent.getBooleanExtra("isShortcut", false)) {
            AlarmHelper.getInstance().init(applicationContext)
        }

        mViewModel = createViewModel()
        mViewModel.searchResultLiveData.observe(this, Observer<List<Product>> { response -> updateViewState(response) })

        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mAdapter = RecyclerViewAdapter()
        mRecyclerView.adapter = mAdapter
        llEmptyView.visible()

        mAdapter.setOnProductClickListener(object : RecyclerViewAdapter.ClickListener {
            override fun onProductClick(v: View, position: Int) {

                val product = mAdapter.getProductAtPosition(position)
                showProductDetailsActivity(product)
            }
        })
    }

    private fun updateViewState(products: List<Product>) = if (products.isEmpty()) showEmptyView(false)
        else showProductList(products)

    private fun showEmptyView(isSearchFieldEmpty: Boolean) {
        mAdapter.updateData(arrayListOf())
        llEmptyView.visible()
        if (isSearchFieldEmpty) {
            ivEmptyIllustration.setImageDrawable(AppCompatResources.getDrawable(this,
                    R.drawable.empty))
            tvEmptyTitle.text = getString(R.string.search_view_empty_text)
        } else {
            tvEmptyTitle.text = getString(R.string.search_view_not_found_text)
        }
    }

    private fun showProductList(products: List<Product>) {
        llEmptyView.gone()
        mAdapter.updateData(products)
    }

    private fun createViewModel() = ViewModelProviders.of(this).get(
        SearchProductsViewModel(
            application
        )::class.java)

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.search, menu)
        val searchView = menu.findItem(R.id.search).actionView as SearchView
        searchView.isIconified = false

        val close: ImageView = searchView.findViewById(androidx.appcompat.R.id.search_close_btn)
        close.setColorFilter(ContextCompat.getColor(this, R.color.black))
        val searchText: TextView = searchView.findViewById(androidx.appcompat.R.id.search_src_text)
        close.setOnClickListener { searchText.text = "" }
        searchView.setOnQueryTextListener(this)
        searchView.queryHint = getString(R.string.search)
//        searchText.setBackgroundResource(R.drawable.search_view_background)
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
        return false
    }

    override fun onQueryTextSubmit(query: String?) = false

    override fun onQueryTextChange(newText: String): Boolean {
        if (newText.isEmpty()) {
            showEmptyView(true)
        } else mViewModel.searchInputLiveData.value = newText
        return true
    }
}
