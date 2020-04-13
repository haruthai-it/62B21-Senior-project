package com.sp62b21.myapp.vm.product

import android.app.Application
import com.sp62b21.myapp.data.models.Product
import com.sp62b21.myapp.vm.base.BaseViewModel

class DeleteProductViewModel(app: Application) : BaseViewModel(app) {
    fun deleteProduct(product: Product) = productRepository.deleteProduct(product)
}