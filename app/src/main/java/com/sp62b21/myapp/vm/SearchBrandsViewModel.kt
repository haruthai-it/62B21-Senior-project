package com.sp62b21.myapp.vm

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.sp62b21.myapp.data.models.Brand
import com.sp62b21.myapp.vm.base.BaseViewModel

class SearchBrandsViewModel(app: Application) : BaseViewModel(app) {
    val liveData = brandRepository.getAllBrandsLiveData()

    val searchInputLiveData: MutableLiveData<String> = MutableLiveData()
    val searchResultLiveData: LiveData<List<Brand>> = Transformations.switchMap(searchInputLiveData) {
        if (it.isNotEmpty()) {
            brandRepository.getBrandsForSearch(it)
        } else {
            MutableLiveData()
        }
    }

    fun deleteBrand(brand: Brand) = brandRepository.deleteBrand(brand)

    fun saveBrand(brand: Brand) = brandRepository.saveBrand(brand)
}