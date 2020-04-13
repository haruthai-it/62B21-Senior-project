package com.sp62b21.myapp.data.database

import android.app.Application
import androidx.lifecycle.LiveData
import com.sp62b21.myapp.data.models.Category
import io.reactivex.Observable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class CategoryListRepository(app: Application) {

//    val allWords: LiveData<List<Word>> = wordDao.getAlphabetizedWords()
    private val mCategoryDao = AppDatabase.getInstance(app).categoryDao()
//    private val mAllCategoriesLiveData = mCategoryDao.getAllCategoriesLiveData()
    val mAllCategoriesLiveData: LiveData<List<Category>> = mCategoryDao.getAllCategoriesLiveData()

    fun getAllCategoriesLiveData() = mAllCategoriesLiveData

//    fun deleteAllCategories() = Completable.fromCallable { mCategoryDao.deleteAllCategories() }.subscribeOn(Schedulers.io()).subscribe()!!
    fun deleteAllCategories() = mCategoryDao.deleteAllCategories()

//    fun saveCategory(category: Category) = Completable.fromCallable { mCategoryDao.saveCategory(category) }.subscribeOn(Schedulers.io()).subscribe()!!
    fun saveCategory(category: Category) = mCategoryDao.saveCategory(category)

//    fun deleteCategory(category: Category) = Completable.fromCallable { mCategoryDao.deleteCategory(category) }.subscribeOn(Schedulers.io()).subscribe()!!
    fun deleteCategory(category: Category) = mCategoryDao.deleteCategory(category)

//    fun updateCategory(category: Category) = Completable.fromCallable { mCategoryDao.updateCategory(category) }.subscribeOn(Schedulers.io()).subscribe()!!
    fun updateCategory(category: Category) = mCategoryDao.updateCategory(category)

//    fun updateCategoryOrder(categories: List<Category>) = Completable.fromCallable { mCategoryDao.updateCategoryOrder(categories) }.subscribeOn(Schedulers.io()).subscribe()!!

    fun getCategoriesForSearch(searchText: String) = mCategoryDao.getCategoriesForSearch(searchText)

    fun getAllCategories(): ArrayList<Category> {
        val categoryList = arrayListOf<Category>()
        Observable.fromCallable { mCategoryDao.getAllCategories() }.subscribeOn(Schedulers.io())
                .flatMap { categories -> Observable.fromIterable(categories) }
                .subscribeBy(onNext = { category -> categoryList.add(category) })
        return categoryList
    }
}