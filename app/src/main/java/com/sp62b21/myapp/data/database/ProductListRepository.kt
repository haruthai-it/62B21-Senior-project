package com.sp62b21.myapp.data.database

import android.app.Application
import androidx.lifecycle.LiveData
import com.sp62b21.myapp.data.models.Product
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers


class ProductListRepository(app: Application) {

    private val mProductDao = AppDatabase.getInstance(app).productDao()

    val mAllProductsLiveData: LiveData<List<Product>> = mProductDao.getAllProductsLiveData()

    fun getAllProductsLiveData() = mAllProductsLiveData

    fun deleteAllProducts() = Completable.fromCallable { mProductDao.deleteAllProducts() }.subscribeOn(Schedulers.io()).subscribe()!!

    fun saveProduct(product: Product) = Completable.fromCallable { mProductDao.saveProduct(product) }.subscribeOn(Schedulers.io()).subscribe()!!

    fun deleteProduct(product: Product) = Completable.fromCallable { mProductDao.deleteProduct(product) }.subscribeOn(Schedulers.io()).subscribe()!!

    fun updateProduct(product: Product) = Completable.fromCallable { mProductDao.updateProduct(product) }.subscribeOn(Schedulers.io()).subscribe()!!

    fun getProductsForSearch(searchText: String) = mProductDao.getProductsForSearch(searchText)

    fun getAllProducts(): ArrayList<Product> {
        val productList = arrayListOf<Product>()
//        Observable.fromCallable { mProductDao.getAllProducts() }.subscribeOn(Schedulers.io())
//                .flatMap { products -> Observable.fromIterable(products) }
//                .subscribeBy(onNext = { product -> productList.add(product) })
        return productList
    }
}