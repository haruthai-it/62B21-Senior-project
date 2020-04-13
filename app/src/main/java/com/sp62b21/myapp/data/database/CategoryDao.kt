package com.sp62b21.myapp.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.sp62b21.myapp.data.models.Category

@Dao
interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveCategory(category: Category)

    @Update
    fun updateCategory(category: Category)

    @Update
    fun updateCategoryOrder(Categories: List<Category>)

    @Delete
    fun deleteCategory(category: Category)

    @Query("DELETE FROM Category")
    fun deleteAllCategories()

    @Insert
    fun insertAll(Categories: List<Category>)

    @Query("SELECT * FROM Category ORDER BY category_name")
    fun getAllCategoriesLiveData(): LiveData<List<Category>>

    @Query("SELECT * FROM Category ORDER BY category_name")
    fun getAllCategories(): List<Category>

    @Query("SELECT * FROM Category WHERE category_name LIKE '%' || :searchText || '%'")
    fun getCategoriesForSearch(searchText: String): LiveData<List<Category>>
}