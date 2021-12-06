package com.misakanetwork.kotlinlab.jetpack.dao

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.misakanetwork.kotlinlab.MyApplication
import com.misakanetwork.kotlinlab.jetpack.bean.ArticleDataBean

/**
 * Created By：Misaka10085
 * on：2021/12/6
 * package：com.misakanetwork.kotlinlab.jetpack.dao
 * class name：ArticleDatabase
 * desc：ArticleDatabase
 */
@Database(entities = [ArticleDataBean::class], version = 1, exportSchema = true)
abstract class ArticleDatabase : RoomDatabase() {

    abstract fun getArticleDao(): ArticleDao

    companion object {
        val instance = Single.sin
        const val DATABASE_NAME = "article.db"
    }

    private object Single {
        val sin: ArticleDatabase = Room.databaseBuilder(
            MyApplication.instance(),
            ArticleDatabase::class.java,
            DATABASE_NAME
        )
//            .allowMainThreadQueries() // 允许在主线程进行数据操作
            .build()
    }
}