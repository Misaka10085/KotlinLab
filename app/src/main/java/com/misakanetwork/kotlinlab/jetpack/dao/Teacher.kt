package com.misakanetwork.kotlinlab.jetpack.dao

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

/**
 * Created By：Misaka10085
 * on：2021/11/29
 * package：com.misakanetwork.kotlinlab.jetpack.dao
 * class name：Teacher
 * desc：table teacher
 */
@Entity(tableName = "teacher.db")
data class Teacher(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id", typeAffinity = ColumnInfo.INTEGER)
    var id: Int = 0,

    @ColumnInfo(name = "name", typeAffinity = ColumnInfo.TEXT)
    var name: String = "",

    @ColumnInfo(name = "age", typeAffinity = ColumnInfo.INTEGER)
    var age: Int = 0,

//    @ColumnInfo(name = "sex", typeAffinity = ColumnInfo.INTEGER)
//    var sex: Int = 0,

    @ColumnInfo(name = "sex", typeAffinity = ColumnInfo.TEXT)
    var sex: String? = null,

    @ColumnInfo(name = "bar_data", typeAffinity = ColumnInfo.INTEGER)
    var barData: Int = 0
) {
    @Ignore
    constructor() : this(0) // 解决创建仅包含id的Student对象时编译失败的问题
}