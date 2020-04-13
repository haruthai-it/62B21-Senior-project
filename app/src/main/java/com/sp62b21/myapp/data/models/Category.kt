package com.sp62b21.myapp.data.models

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Category (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "category_id")
    val id: Int,
    @ColumnInfo(name = "category_name")
    var name: String,
    @ColumnInfo(name = "category_group")
    var group: String
)


//    constructor(id: Long, title: String, group: String) {
//        this.id = id
//        this.title = title
//        this.group = group
//    }
//}