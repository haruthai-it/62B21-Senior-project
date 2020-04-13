package com.sp62b21.myapp.vm.product

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.sp62b21.myapp.data.models.Product
import com.sp62b21.myapp.vm.base.BaseViewModel

class SearchProductsViewModel(app: Application) : BaseViewModel(app) {
    val searchInputLiveData: MutableLiveData<String> = MutableLiveData()
    val searchResultLiveData: LiveData<List<Product>> = Transformations.switchMap(searchInputLiveData) {
        if (it.isNotEmpty()) {
            productRepository.getProductsForSearch(it)
        } else {
            MutableLiveData()
        }
    }
}