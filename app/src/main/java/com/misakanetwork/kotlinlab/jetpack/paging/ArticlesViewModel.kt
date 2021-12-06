package com.misakanetwork.kotlinlab.jetpack.paging

import android.os.AsyncTask
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.misakanetwork.kotlinlab.jetpack.bean.ArticleDataBean
import com.misakanetwork.kotlinlab.jetpack.dao.ArticleDao
import com.misakanetwork.kotlinlab.jetpack.dao.ArticleDatabase

/**
 * Created By：Misaka10085
 * on：2021/12/3
 * package：com.misakanetwork.kotlinlab.jetpack.paging
 * class name：ArticlesViewModel
 * desc：ArticlesViewModel
 */
class ArticlesViewModel : ViewModel() {
    var articlePagedList: LiveData<PagedList<ArticleDataBean>>

    init {
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false) // 设置控件占位，若当前加载加载数据量不足总量则为剩下的未加载数据留存位置(默认开启，过于占用内存)
//            .setPageSize(ArticlesPositionalDataSource.PER_PAGE) // 设置pageSize，与接口传参的size相同或与约定的size相同
            .setPageSize(ArticlesPageKeyedDataSource.PER_PAGE)
            .setPrefetchDistance(5) // 设置当距离底部还有多少条数据的时候就开始加载下一页
//            .setInitialLoadSizeHint(ArticlesPositionalDataSource.PER_PAGE * 2) // 设置首次请求多少条数据
            .setInitialLoadSizeHint(ArticlesPageKeyedDataSource.PER_PAGE)
//            .setMaxSize(65536 * ArticlesPositionalDataSource.PER_PAGE) // 限制总共能加载数量
            .setMaxSize(65536 * ArticlesPageKeyedDataSource.PER_PAGE)
            .build()
        articlePagedList = LivePagedListBuilder<Int, ArticleDataBean>(
            ArticlesDataSourceFactory(),
            config
        ).build()

        // 从数据库取数据
//        val articleDao = ArticleDatabase.instance.getArticleDao()
//        articlePagedList = LivePagedListBuilder<Int, ArticleDataBean>(
//            articleDao.articleList(),
//            8
//        )
//            .setBoundaryCallback(ArticleBoundaryCallback())
//            .build()
    }

//    /**
//     * 从数据库取-刷新数据
//     */
//    fun refresh() {
//        AsyncTask.execute {
//            ArticleDatabase.instance
//                .getArticleDao()
//                .clear()
//        }
//    }
}