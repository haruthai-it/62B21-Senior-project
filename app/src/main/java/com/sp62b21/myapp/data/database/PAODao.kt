package com.sp62b21.myapp.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.sp62b21.myapp.data.models.PAO

@Dao
interface PAODao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPAO(pao: PAO)

    @Update
    fun updatePAO(pao: PAO)

    @Update
    fun updatePAOOrder(PAOs: List<PAO>)

    @Delete
    fun deletePAO(pao: PAO)

    @Insert
    fun insertAll(PAOs: List<PAO>)

    @Query("DELETE FROM PAO")
    fun deleteAllPAOs()

    @Query("SELECT * FROM PAO ORDER BY pao_id")
    fun getAllPAOsLiveData(): LiveData<List<PAO>>

    @Query("SELECT * FROM PAO ORDER BY pao_id")
    fun getAllPAOs(): List<PAO>

    @Query("SELECT * FROM PAO WHERE pao_month LIKE '%' || :searchText || '%'")
    fun getPAOsForSearch(searchText: String): LiveData<List<PAO>>
}