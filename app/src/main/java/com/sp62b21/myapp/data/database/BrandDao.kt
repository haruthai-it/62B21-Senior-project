package com.sp62b21.myapp.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.sp62b21.myapp.data.models.Brand

@Dao
interface BrandDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveBrand(brand: Brand)

    @Update
    fun updateBrand(brand: Brand)

    @Update
    fun updateBrandOrder(Brands: List<Brand>)

    @Delete
    fun deleteBrand(brand: Brand)

    @Query("DELETE FROM Brand")
    fun deleteAllBrands()

    @Insert
    fun insertAll(Brands: List<Brand>)

    @Query("SELECT * FROM Brand WHERE brand_id=:id ")
//    fun getBrandById(id: Int): LiveData<Brand>
    fun getBrandById(id: Int): LiveData<Brand>

    @Query("SELECT * FROM Brand ORDER BY brand_name")
    fun getAllBrandsLiveData(): LiveData<List<Brand>>

    @Query("SELECT * FROM Brand ORDER BY brand_name")
    fun getAllBrands(): List<Brand>

    @Query("SELECT * FROM Brand WHERE brand_name LIKE '%' || :searchText || '%'")
    fun getBrandsForSearch(searchText: String): LiveData<List<Brand>>
}