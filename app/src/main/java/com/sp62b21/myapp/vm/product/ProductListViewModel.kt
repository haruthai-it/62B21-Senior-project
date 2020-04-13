package com.sp62b21.myapp.vm.product

import android.app.Application
import com.sp62b21.myapp.data.models.Product
import com.sp62b21.myapp.vm.base.BaseViewModel

class ProductListViewModel(app: Application) : BaseViewModel(app) {

    val liveData = productRepository.getAllProductsLiveData()

//    fun updateProductOrder(products: List<Product>) = productRepository.updateProductOrder(products)

    fun deleteProduct(product: Product) = productRepository.deleteProduct(product)

    fun saveProduct(product: Product) = productRepository.saveProduct(product)
}