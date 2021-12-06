package com.misakanetwork.kotlinlab.jetpack.api

import com.misakanetwork.kotlinlab.jetpack.bean.ArticlesBean
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created By：Misaka10085
 * on：2021/12/2
 * package：com.misakanetwork.kotlinlab.jetpack.api
 * class name：JetpackApi
 * desc：api
 */
interface JetpackApi {
    @GET("article/list/{page}/json")
    fun getArticles(@Path("page") page: Int, @Query("page_size") pageSize: Int): Call<ArticlesBean>
}