package com.sp62b21.myapp.vm.product

import android.app.Application
import com.sp62b21.myapp.data.models.Brand
import com.sp62b21.myapp.data.models.Product
import com.sp62b21.myapp.vm.base.BaseViewModel

class EditProductViewModel(app: Application) : BaseViewModel(app) {
//    val brandInputLiveData: MutableLiveData<Int> = MutableLiveData()
//    val brandResultLiveData: LiveData<Brand> = Transformations.switchMap(brandInputLiveData) {
//        if (it!=null) {
//            brandRepository.getBrandById(it)
//        } else {
//            MutableLiveData()
//        }
//    }
    val brandLiveData = brandRepository.getAllBrandsLiveData()

    val brandList = brandRepository.mAllBrands

    val paoList = paoRepository.mAllPAOs

    fun getAllBrands1(): List<Brand> {
        return brandRepository.getAllBrands()
    }

//    val brandList = brandRepository.getAllBrands()

//    val brandList = brandRepository.getBrandsList()

    fun getBrandById(id: Int) = brandRepository.getBrandById(id)

    fun updateProduct(product: Product) = productRepository.updateProduct(product)
}