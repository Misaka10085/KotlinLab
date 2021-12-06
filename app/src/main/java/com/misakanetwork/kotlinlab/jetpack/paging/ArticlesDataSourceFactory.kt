package com.misakanetwork.kotlinlab.jetpack.paging

import androidx.paging.DataSource
import com.misakanetwork.kotlinlab.jetpack.bean.ArticleDataBean

/**
 * Created By：Misaka10085
 * on：2021/12/3
 * package：com.misakanetwork.kotlinlab.jetpack.paging
 * class name：ArticlesDataSourceFactory
 * desc：ArticlesDataSourceFactory
 */
class ArticlesDataSourceFactory : DataSource.Factory<Int?, ArticleDataBean>() {

    override fun create(): DataSource<Int?, ArticleDataBean> {
//        return ArticlesPositionalDataSource()
        return ArticlesPageKeyedDataSource()
    }
}