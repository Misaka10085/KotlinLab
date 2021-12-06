package com.misakanetwork.kotlinlab.jetpack.paging

import android.util.Log
import androidx.paging.PageKeyedDataSource
import com.misakanetwork.kotlinlab.jetpack.api.JetpackRetrofitClient
import com.misakanetwork.kotlinlab.jetpack.bean.ArticleDataBean
import com.misakanetwork.kotlinlab.jetpack.bean.ArticlesBean
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created By：Misaka10085
 * on：2021/12/3
 * package：com.misakanetwork.kotlinlab.jetpack.paging
 * class name：ArticlesPageKeyedDataSource
 * desc：PageKeyDataSource 适用于按页码分页
 */
class ArticlesPageKeyedDataSource : PageKeyedDataSource<Int, ArticleDataBean>() {

    companion object {
        const val PER_PAGE = 20 // 每页加载size
        const val FIRST_PAGE = 0 // 第一页页码
    }

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, ArticleDataBean>
    ) {
        JetpackRetrofitClient.instance
            ?.getApi()
            ?.getArticles(FIRST_PAGE, PER_PAGE)
            ?.enqueue(object : Callback<ArticlesBean> {
                override fun onResponse(
                    call: Call<ArticlesBean>,
                    response: Response<ArticlesBean>
                ) {
                    callback.onResult(
                        response.body()!!.data.datas,
                        null, // 上一页
                        FIRST_PAGE // 下一页
                    )
                    Log.e(
                        "loadInitial",
                        "loadInitial: count:${response.body()!!.data.datas.size} ${response.body()?.data?.datas.toString()}"
                    )
                }

                override fun onFailure(call: Call<ArticlesBean>, t: Throwable) {
                    Log.e("loadInitial", "onFailure ${t.message}")
                }
            })
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, ArticleDataBean>) {
    }

    /**
     * 加载下一页
     */
    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, ArticleDataBean>) {
        JetpackRetrofitClient.instance
            ?.getApi()
            ?.getArticles(params.key, PER_PAGE) // params的key页码会自动增长
            ?.enqueue(object : Callback<ArticlesBean> {
                override fun onResponse(
                    call: Call<ArticlesBean>,
                    response: Response<ArticlesBean>
                ) {
                    // 下一页页码，判断是否还有剩余分页
                    val nextKey =
                        response.body()!!.data.curPage.let { if (it < response.body()!!.data.total) params.key + 1 else null }

                    callback.onResult(
                        response.body()!!.data.datas,
                        nextKey // 下一页
                    )
                    Log.e(
                        "loadAfter",
                        "loadAfter: count:${response.body()!!.data.datas.size} ${response.body()?.data?.datas.toString()}"
                    )
                }

                override fun onFailure(call: Call<ArticlesBean>, t: Throwable) {
                    Log.e("loadAfter", "onFailure ${t.message}")
                }
            })
    }
}