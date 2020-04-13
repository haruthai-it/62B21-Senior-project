package com.sp62b21.myapp.data.models


import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class Brand (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "brand_id")
    val id: Int,
    @ColumnInfo(name = "brand_name")
    var name: String = ""

) : Parcelable
{
    @Ignore
    constructor() : this(0,"")
}