package com.sp62b21.myapp.data.database

import android.app.Application
import androidx.lifecycle.LiveData
import com.sp62b21.myapp.data.models.Brand
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import android.util.Log


class BrandListRepository(app: Application) {

//    val allWords: LiveData<List<Word>> = wordDao.getAlphabetizedWords()
//    private val mProductDao = AppDatabase.getInstance(app).productDao()
    private val mBrandDao = AppDatabase.getInstance(app).brandDao()
//    private val mAllBrandsLiveData = mBrandDao.getAllBrandsLiveData()
    val mAllBrandsLiveData: LiveData<List<Brand>> = mBrandDao.getAllBrandsLiveData()

    val mAllBrands: List<Brand> = mBrandDao.getAllBrands()
//    fun getBrandsList() = mAllBrands

    fun getAllBrandsLiveData() = mAllBrandsLiveData

    fun deleteAllBrands() = Completable.fromCallable { mBrandDao.deleteAllBrands() }.subscribeOn(Schedulers.io()).subscribe()!!
//    fun deleteAllBrands() = mBrandDao.deleteAllBrands()

    fun saveBrand(brand: Brand) = Completable.fromCallable { mBrandDao.saveBrand(brand) }.subscribeOn(Schedulers.io()).subscribe()!!
//    fun saveBrand(brand: Brand) = mBrandDao.saveBrand(brand)

    fun deleteBrand(brand: Brand) = Completable.fromCallable { mBrandDao.deleteBrand(brand) }.subscribeOn(Schedulers.io()).subscribe()!!
//    fun deleteBrand(brand: Brand) = mBrandDao.deleteBrand(brand)

    fun updateBrand(brand: Brand) = Completable.fromCallable { mBrandDao.updateBrand(brand) }.subscribeOn(Schedulers.io()).subscribe()!!
//    fun updateBrand(brand: Brand) = mBrandDao.updateBrand(brand)

//    fun updateBrandOrder(brands: List<Brand>) = Completable.fromCallable { mBrandDao.updateBrandOrder(brands) }.subscribeOn(Schedulers.io()).subscribe()!!
    fun getBrandById(id: Int) = mBrandDao.getBrandById(id)

    fun getBrandsForSearch(searchText: String) = mBrandDao.getBrandsForSearch(searchText)

    fun getAllBrands(): ArrayList<Brand> {
        val brandList = arrayListOf<Brand>()
        Log.i("AAA", "start")
        mBrandDao.getAllBrands()
        val observe= Observable.fromCallable { mBrandDao.getAllBrands() }.subscribeOn(Schedulers.io())
                .flatMap {
                    Log.i("BBB", it.toString())
                    Observable.fromIterable(it)
                }
                .subscribeBy(onNext = {
                    Log.i("CCC", it.toString())
                    brandList.add(it)
                })
        Log.i("DDD", ""+brandList.size)
        return brandList
    }
}