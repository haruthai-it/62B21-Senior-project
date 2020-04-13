package com.sp62b21.myapp.data.models

import androidx.annotation.NonNull
import androidx.room.*
import java.util.*

@Entity(tableName = "products_table"
    )
data class Product(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "product_id")
    @NonNull
    val id: Long,

    @ColumnInfo(name = "product_title")
    var title: String,

    @ColumnInfo(name = "product_brand")
    var brand: String?,

    @ColumnInfo(name = "product_category")
    var category: Int?,

    @ColumnInfo(name = "product_opened")
    @NonNull
    var openedDate: Long,

    @ColumnInfo(name = "product_pao")
    var pao: Int?,

    @ColumnInfo(name = "product_bb")
    var bb: Long?,

    @ColumnInfo(name = "product_expired")
    var expiredDate: Long,

    @ColumnInfo(name = "product_note")
    var note: String? = "",

    @ColumnInfo(name = "product_image")
    var image: String?,

    @ColumnInfo(name = "product_time_stamp")
    @NonNull
    var timeStamp: Long
){

    @Ignore
    constructor() : this(0,"",null,null,0,null,0L,0L,"","", Date().time)
}
