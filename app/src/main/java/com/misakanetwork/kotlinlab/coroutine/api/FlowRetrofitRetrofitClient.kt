package com.misakanetwork.kotlinlab.coroutine.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created By：Misaka10085
 * on：2022/1/20
 * package：com.misakanetwork.kotlinlab.coroutine.api
 * class name：FlowRetrofitRetrofitClient
 * desc：FlowRetrofitRetrofitClient
 */
object FlowRetrofitRetrofitClient {

    private val instance: Retrofit by lazy {
        Retrofit.Builder()
            .client(OkHttpClient.Builder().build())
            .baseUrl("https://www.wanandroid.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val articleApi: FlowRetrofitApi by lazy {
        instance.create(FlowRetrofitApi::class.java)
    }
}