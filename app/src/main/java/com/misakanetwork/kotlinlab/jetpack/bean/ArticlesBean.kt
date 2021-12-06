package com.misakanetwork.kotlinlab.jetpack.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

/**
 * Created By：Misaka10085
 * on：2021/12/2
 * package：com.misakanetwork.kotlinlab.jetpack.bean
 * class name：ArticlesBean
 * desc：ArticlesBean
 */
data class ArticlesBean(
    var `data`: ArticleBean = ArticleBean(),
    var errorCode: Int = 0,
    var errorMsg: String = ""
)

data class ArticleBean(
    var curPage: Int = 0,
    var datas: List<ArticleDataBean> = listOf(),
    var offset: Int = 0,
    var over: Boolean = false,
    var pageCount: Int = 0,
    var size: Int = 0,
    var total: Int = 0
)

@Entity(tableName = "article")
data class ArticleDataBean(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "no", typeAffinity = ColumnInfo.INTEGER)
    var NO: Int = 0, // 自定义存入数据库的主键
    @ColumnInfo(name = "desc", typeAffinity = ColumnInfo.TEXT)
    var desc: String = "",
    @ColumnInfo(name = "id", typeAffinity = ColumnInfo.INTEGER)
    var id: Int = 0,
    @ColumnInfo(name = "title", typeAffinity = ColumnInfo.TEXT)
    var title: String = "",
) {
    @Ignore
    constructor() : this(0)
}