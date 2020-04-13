package com.sp62b21.myapp.data.database

import android.app.Application
import androidx.lifecycle.LiveData
import com.sp62b21.myapp.data.models.PAO
import io.reactivex.Observable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class PAOListRepository(app: Application) {

//    val allWords: LiveData<List<Word>> = wordDao.getAlphabetizedWords()
    private val mPAODao = AppDatabase.getInstance(app).paoDao()
//    private val mAllPAOsLiveData = mPAODao.getAllPAOsLiveData()
//    private val mProductDao = AppDatabase.getInstance(app).taskDAO()

    val mAllPAOsLiveData: LiveData<List<PAO>> = mPAODao.getAllPAOsLiveData()
    val mAllPAOs: List<PAO> = mPAODao.getAllPAOs()

    fun getAllPAOsLiveData() = mAllPAOsLiveData

    fun getAllPAO() = mAllPAOs

//    fun deleteAllPAOs() = Completable.fromCallable { mPAODao.deleteAllPAOs() }.subscribeOn(Schedulers.io()).subscribe()!!
    fun deleteAllPAOs() = mPAODao.deleteAllPAOs()

//    fun savePAO(PAO: PAO) = Completable.fromCallable { mPAODao.savePAO(PAO) }.subscribeOn(Schedulers.io()).subscribe()!!

//    fun deletePAO(PAO: PAO) = Completable.fromCallable { mPAODao.deletePAO(PAO) }.subscribeOn(Schedulers.io()).subscribe()!!
    fun deletePAO(PAO: PAO) = mPAODao.deletePAO(PAO)

//    fun updatePAO(PAO: PAO) = Completable.fromCallable { mPAODao.updatePAO(PAO) }.subscribeOn(Schedulers.io()).subscribe()!!
    fun updatePAO(PAO: PAO) = mPAODao.updatePAO(PAO)

//    fun updatePAOOrder(PAOs: List<PAO>) = Completable.fromCallable { mPAODao.updatePAOOrder(PAOs) }.subscribeOn(Schedulers.io()).subscribe()!!

    fun getPAOsForSearch(searchText: String) = mPAODao.getPAOsForSearch(searchText)

    fun getAllPAOs(): ArrayList<PAO> {
        val PAOList = arrayListOf<PAO>()
        Observable.fromCallable { mPAODao.getAllPAOs() }.subscribeOn(Schedulers.io())
                .flatMap { PAOs -> Observable.fromIterable(PAOs) }
                .subscribeBy(onNext = { PAO -> PAOList.add(PAO) })
        return PAOList
    }
}