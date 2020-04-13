package com.sp62b21.myapp.vm.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.sp62b21.myapp.data.database.*

abstract class BaseViewModel(app: Application) : AndroidViewModel(app) {
    // The ViewModel maintains a reference to the repository to get data.
    val productRepository = ProductListRepository(app)
    val brandRepository= BrandListRepository(app)
    val categoryRepository= CategoryListRepository(app)
    val paoRepository= PAOListRepository(app)

    // LiveData gives us updated words when they change.
//    val mselectedBrand: LiveData<Brand>
//    val mAllProductsLiveData: LiveData<List<Product>>
//    val mAllBrandsLiveData: LiveData<List<Brand>>
//    val mAllCategoriesLiveData: LiveData<List<Category>>
//    val mAllPAOs: List<PAO>

    init {
        // Gets reference to WordDao from WordRoomDatabase to construct
        // the correct WordRepository.
//        val mProductDao = AppDatabase.getAppDatabase(app,viewModelScope)!!.productDao()
//        productRepository = ProductListRepository(mProductDao)
//        mAllProductsLiveData = productRepository.mAllProductsLiveData
//
//        val mBrandDao = AppDatabase.getAppDatabase(app,viewModelScope)!!.brandDao()
//        brandRepository = BrandListRepository(mBrandDao)
//        mAllBrandsLiveData = brandRepository.mAllBrandsLiveData
//
//        val mCategoryDao = AppDatabase.getAppDatabase(app,viewModelScope)!!.categoryDao()
//        categoryRepository = CategoryListRepository(mCategoryDao)
//        mAllCategoriesLiveData = categoryRepository.mAllCategoriesLiveData
//
//        val mPAODao = AppDatabase.getAppDatabase(app,viewModelScope)!!.paoDao()
//        paoRepository = PAOListRepository(mPAODao)
//        mAllPAOs = paoRepository.mAllPAOs

    }


}

//// The ViewModel maintains a reference to the repository to get data.
//private val repository: WordRepository
//// LiveData gives us updated words when they change.
//val allWords: LiveData<List<Word>>
//
//init {
//    // Gets reference to WordDao from WordRoomDatabase to construct
//    // the correct WordRepository.
//    val wordsDao = WordRoomDatabase.getDatabase(application).wordDao()
//    repository = WordRepository(wordsDao)
//    allWords = repository.allWords
//}

/**
 * The implementation of insert() in the database is completely hidden from the UI.
 * Room ensures that you're not doing any long running operations on
 * the main thread, blocking the UI, so we don't need to handle changing Dispatchers.
 * ViewModels have a coroutine scope based on their lifecycle called
 * viewModelScope which we can use here.
// */
//fun insert(word: Word) = viewModelScope.launch {
//    repository.insert(word)
//}