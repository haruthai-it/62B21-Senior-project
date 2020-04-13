package com.sp62b21.myapp.vm.product

import android.app.Application
import com.sp62b21.myapp.data.models.Product
import com.sp62b21.myapp.vm.base.BaseViewModel

class AddProductViewModel(app: Application) : BaseViewModel(app) {
    fun saveProduct(product: Product) = productRepository.saveProduct(product)
}