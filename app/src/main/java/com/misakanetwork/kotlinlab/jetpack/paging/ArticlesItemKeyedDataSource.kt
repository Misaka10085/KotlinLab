package com.misakanetwork.kotlinlab.jetpack.paging

import android.util.Log
import androidx.paging.ItemKeyedDataSource
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
 * class name：ArticlesItemKeyedDataSource
 * desc：ItemKeyedDataSource 适用于下一页根据上一页最后一个对象的某个字段作为id(key)的情况，如评论功能 since=9001&pageSize=5，则返回9001之后的5条数据
 */
//class ArticlesItemKeyedDataSource : ItemKeyedDataSource<Int, ArticleDataBean>() {
//
//    companion object {
//        const val PER_PAGE = 8
//    }
//
//    override fun getKey(item: ArticleDataBean): Int {
//        return item.id // 返回最后一个对象的id字段作为请求参数的key
//    }
//
//    override fun loadInitial(
//        params: LoadInitialParams<Int>,
//        callback: LoadInitialCallback<ArticleDataBean>
//    ) {
//        val since = 0
//        JetpackRetrofitClient.instance
//            ?.getApi()
//            ?.getArticles(
//                since,
//                PER_PAGE
//            )
//            ?.enqueue(object : Callback<ArticleDataBean> {
//                override fun onResponse(
//                    call: Call<ArticleDataBean>,
//                    response: Response<ArticleDataBean>
//                ) {
//                    if (response.body() != null) {
//                        callback.onResult(response.body())
//                    }
//                }
//
//                override fun onFailure(call: Call<ArticleDataBean>, t: Throwable) {
//                }
//            })
//    }
//
//    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<ArticleDataBean>) {
//        JetpackRetrofitClient.instance
//            ?.getApi()
//            ?.getArticles(
//                params.key, // 该key为上一页最后一个对象的id
//                PER_PAGE
//            )
//            ?.enqueue(object : Callback<ArticleDataBean> {
//                override fun onResponse(
//                    call: Call<ArticleDataBean>,
//                    response: Response<ArticleDataBean>
//                ) {
//                    if (response.body() != null) {
//                        callback.onResult(response.body())
//                    }
//                }
//
//                override fun onFailure(call: Call<ArticleDataBean>, t: Throwable) {
//                }
//            })
//    }
//
//    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<ArticleDataBean>) {
//    }
//}