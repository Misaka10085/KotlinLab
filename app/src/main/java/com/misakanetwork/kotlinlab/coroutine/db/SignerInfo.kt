package com.misakanetwork.kotlinlab.coroutine.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created By：Misaka10085
 * on：2022/1/20
 * package：com.misakanetwork.kotlinlab.coroutine.db
 * class name：SignerInfo
 * desc：SignerInfo
 */
@Entity
data class SignerInfo(
    @PrimaryKey
    val uid: Int,
    @ColumnInfo(name = "first_name")
    val firstName: String,
    @ColumnInfo(name = "last_name")
    val lastName: String
)