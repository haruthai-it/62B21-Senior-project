package com.sp62b21.myapp.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.sp62b21.myapp.data.models.Product

@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveProduct(product: Product)

    @Update
    fun updateProduct(product: Product)

    @Update
    fun updateProductOrder(Products: List<Product>)

    @Delete
    fun deleteProduct(product: Product)

    @Query("DELETE FROM products_table")
    fun deleteAllProducts()

    @Query("SELECT * FROM products_table ORDER BY product_expired")
    fun getAllProductsLiveData(): LiveData<List<Product>>

    @Query("SELECT * FROM products_table ORDER BY product_expired")
    fun getAllProducts(): List<Product>

    @Query("SELECT * FROM products_table WHERE product_title LIKE '%' || :searchText || '%'")
    fun getProductsForSearch(searchText: String): LiveData<List<Product>>
}