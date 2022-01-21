package com.misakanetwork.kotlinlab.jetpack.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.misakanetwork.kotlinlab.jetpack.bean.ArticleDataBean

/**
 * Created By：Misaka10085
 * on：2021/12/6
 * package：com.misakanetwork.kotlinlab.jetpack.dao
 * class name：ArticleDao
 * desc：ArticleDao
 */
@Dao
interface ArticleDao {
    @Insert
    fun insertArticles(articles: List<ArticleDataBean>)

    @Query("DELETE FROM article")
    fun clear()

    @Query("SELECT * FROM article")
    fun articleList(): androidx.paging.DataSource.Factory<Int, ArticleDataBean>
}