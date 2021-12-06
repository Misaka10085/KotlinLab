package com.misakanetwork.kotlinlab.coroutine.api

import android.util.Log
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created By：Misaka10085
 * on：2021/11/24
 * package：com.misakanetwork.kotlinlab.coroutine.api
 * class name：UserServiceApi
 * desc：UserServiceApi
 */
data class User(val msg: String, val data: RequestData)

data class RequestData(
    val id: Int,
    val createData: String,
    val updateData: String,
    val delFlag: Boolean = false,
    val name: String,
    val content: String,
    val type: Int
)

val userServiceApi: UserServiceApi by lazy {
    val retrofit = retrofit2.Retrofit.Builder()
        .client(OkHttpClient.Builder().addInterceptor {
            it.proceed(it.request()).apply {
                Log.e("jason", "request:${code()}")
            }
        }.build())
        .baseUrl("https://qyss.qunyuezhihui.com/qyss/")
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
    retrofit.create(UserServiceApi::class.java)
}

interface UserServiceApi {
    // way of AsyncTask
    @GET("student/systemSet/getSystemSetByType")
    fun getUserSystem(@Query("type") type: Int): Call<User>

    // way of coroutine
    @GET("student/systemSet/getSystemSetByType")
    suspend fun getUserLaws(@Query("type") type: Int): User // suspend:挂起函数
}