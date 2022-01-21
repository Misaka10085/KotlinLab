package com.misakanetwork.kotlinlab.coroutine.api

import com.misakanetwork.kotlinlab.jetpack.bean.ArticlesBean
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created By：Misaka10085
 * on：2022/1/20
 * package：com.misakanetwork.kotlinlab.coroutine.api
 * class name：FlowRetrofitApi
 * desc：FlowRetrofitApi
 */
interface FlowRetrofitApi {

    @GET("article/list/{page}/json")
    suspend fun getArticles(
        @Path("page") page: Int,
        @Query("page_size") pageSize: Int
    ): ArticlesBean
}