package com.misakanetwork.kotlinlab.jetpack.paging

import android.util.Log
import androidx.paging.PositionalDataSource
import com.misakanetwork.kotlinlab.jetpack.api.JetpackRetrofitClient
import com.misakanetwork.kotlinlab.jetpack.bean.ArticleDataBean
import com.misakanetwork.kotlinlab.jetpack.bean.ArticlesBean
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created By：Misaka10085
 * on：2021/12/2
 * package：com.misakanetwork.kotlinlab.jetpack.paging
 * class name：ArticlesPositionalDataSource
 * desc：PositionalDataSource，适用于目标数据总数固定，通过特定的位置加载数据
 */
class ArticlesPositionalDataSource : PositionalDataSource<ArticleDataBean>() {
    companion object {
        const val PER_PAGE = 10 // 从每页startPosition开始加载数据条数
    }

    /**
     * 页面首次加载数据时调用
     */
    override fun loadInitial(
        params: LoadInitialParams,
        callback: LoadInitialCallback<ArticleDataBean>
    ) {
        val startPosition = 0 // 从位置0开始
        JetpackRetrofitClient.instance
            ?.getApi()
            ?.getArticles(
                startPosition, // startPosition[开始位置] 但这里使用的接口是分页不是position，实际情况应为从第几条数据开始
                PER_PAGE // 从开始位置开始加载？条列表数据
            )
            ?.enqueue(object : Callback<ArticlesBean> {
                override fun onResponse(
                    call: Call<ArticlesBean>,
                    response: Response<ArticlesBean>
                ) {
                    if (response.body() == null) return
                    callback.onResult(
                        response.body()?.data?.datas!!,
                        response.body()?.data?.curPage!!,
                        response.body()?.data?.total!!
                    ) // onResult返回数据给pageList，首次加载需要传后台反的当前页码与数据总数
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

    /**
     * 加载下一页时调用
     */
    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<ArticleDataBean>) {
        JetpackRetrofitClient.instance
            ?.getApi()
            ?.getArticles(
                params.startPosition,  // startPosition[开始位置] LoadRangeParams会自动更新开始位置
                PER_PAGE // 从开始位置开始加载？条列表数据
//                PER_PAGE * 2
            )
            ?.enqueue(object : Callback<ArticlesBean> {
                override fun onResponse(
                    call: Call<ArticlesBean>,
                    response: Response<ArticlesBean>
                ) {
                    if (response.body() == null) return
                    callback.onResult(response.body()?.data?.datas!!) // onResult返回数据给pageList
                    Log.e(
                        "loadRange",
                        "loadRange: count:${response.body()!!.data.datas.size}  ${response.body()!!.data.datas}"
                    )
                }

                override fun onFailure(call: Call<ArticlesBean>, t: Throwable) {
                    Log.e("loadRange", "onFailure ${t.message}")
                }
            })
    }
}