package com.misakanetwork.kotlinlab.jetpack.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created By：Misaka10085
 * on：2021/12/2
 * package：com.misakanetwork.kotlinlab.jetpack.api
 * class name：JetpackRetrofitClient
 * desc：retrofitClient
 */
class JetpackRetrofitClient private constructor() {
    private var retrofit: Retrofit

    companion object {
        private const val BASE_URL = "https://www.wanandroid.com/"

        @Volatile
        private var mInstance: JetpackRetrofitClient? = null

        val instance: JetpackRetrofitClient?
            get() {
                if (mInstance == null) {
                    synchronized(JetpackRetrofitClient::class.java) {
                        if (mInstance == null) {
                            mInstance = JetpackRetrofitClient()
                        }
                    }
                }
                return mInstance
            }
    }

    init {
        retrofit = Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(getClient())
            .build()
    }

    private fun getClient(): OkHttpClient {
        return OkHttpClient.Builder().build()
    }

    fun getApi(): JetpackApi {
        return retrofit.create(JetpackApi::class.java)
    }
}