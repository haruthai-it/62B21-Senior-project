package com.sp62b21.myapp.data.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.ArrayList

@Entity
@Parcelize
data class PAO (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "pao_id")
    val id: Int,
    @ColumnInfo(name = "pao_month")
    val month: String
): Parcelable

{

    @Ignore
    constructor(month: String) : this(0, "")

}