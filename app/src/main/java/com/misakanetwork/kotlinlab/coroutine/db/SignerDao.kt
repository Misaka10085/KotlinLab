package com.misakanetwork.kotlinlab.coroutine.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * Created By：Misaka10085
 * on：2022/1/20
 * package：com.misakanetwork.kotlinlab.coroutine.db
 * class name：SignerDao
 * desc：SignerDao
 */
@Dao
interface SignerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE) // 若出现id相同的情况，进行替换
    suspend fun insert(signerInfo: SignerInfo) // Room的suspend关键字会自动创建一个continuation挂起点

    @Query("SELECT * FROM signerinfo")
    fun getAll(): Flow<List<SignerInfo>> // Flow/LiveData创建的Room方法自动创建协程支持，不可再修饰suspend
}