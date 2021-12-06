package com.misakanetwork.kotlinlab.jetpack.paging

import android.os.AsyncTask
import android.util.Log
import androidx.paging.PagedList
import com.misakanetwork.kotlinlab.jetpack.api.JetpackRetrofitClient
import com.misakanetwork.kotlinlab.jetpack.bean.ArticleDataBean
import com.misakanetwork.kotlinlab.jetpack.bean.ArticlesBean
import com.misakanetwork.kotlinlab.jetpack.dao.ArticleDatabase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created By：Misaka10085
 * on：2021/12/6
 * package：com.misakanetwork.kotlinlab.jetpack.paging
 * class name：ArticleBoundaryCallback
 * desc：BoundaryCallback方式 网络请求数据数据库保存操作
 */
class ArticleBoundaryCallback() : PagedList.BoundaryCallback<ArticleDataBean>() {

    companion object {
        const val PER_PAGE = 10 // 从每页startPosition开始加载数据条数
    }

    /**
     * 加载第一页
     */
    override fun onZeroItemsLoaded() {
        super.onZeroItemsLoaded()
        getTopData()
    }

    private fun getTopData() {
        val since = 0
        JetpackRetrofitClient.instance
            ?.getApi()
            ?.getArticles(
                since,
                PER_PAGE
            )
            ?.enqueue(object : Callback<ArticlesBean> {
                override fun onResponse(
                    call: Call<ArticlesBean>,
                    response: Response<ArticlesBean>
                ) {
                    insertArticles(response.body()?.data?.datas)
                }

                override fun onFailure(call: Call<ArticlesBean>, t: Throwable) {
                    Log.e("loadInitial", "onFailure ${t.message}")
                }
            })
    }

    /**
     * 操作数据库，把网络数据保存到数据库
     */
    private fun insertArticles(body: List<ArticleDataBean>?) {
        if (body.isNullOrEmpty()) return
        AsyncTask.execute {
            ArticleDatabase.instance
                .getArticleDao()
                .insertArticles(body)
        }
    }

    /**
     * 加载下一页数据
     */
    override fun onItemAtEndLoaded(itemAtEnd: ArticleDataBean) {
        super.onItemAtEndLoaded(itemAtEnd)
        getTopAfterData(itemAtEnd)
    }

    private fun getTopAfterData(article: ArticleDataBean) {
        JetpackRetrofitClient.instance
            ?.getApi()
            ?.getArticles(article.NO, ArticlesPageKeyedDataSource.PER_PAGE) // params的key页码会自动增长
            ?.enqueue(object : Callback<ArticlesBean> {
                override fun onResponse(
                    call: Call<ArticlesBean>,
                    response: Response<ArticlesBean>
                ) {
                    insertArticles(response.body()?.data?.datas)
                }

                override fun onFailure(call: Call<ArticlesBean>, t: Throwable) {
                    Log.e("loadAfter", "onFailure ${t.message}")
                }
            })
    }
}