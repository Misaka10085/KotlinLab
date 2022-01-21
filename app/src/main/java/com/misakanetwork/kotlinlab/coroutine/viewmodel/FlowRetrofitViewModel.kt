package com.misakanetwork.kotlinlab.coroutine.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.misakanetwork.kotlinlab.coroutine.api.FlowRetrofitRetrofitClient
import com.misakanetwork.kotlinlab.jetpack.bean.ArticlesBean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

/**
 * Created By：Misaka10085
 * on：2022/1/20
 * package：com.misakanetwork.kotlinlab.coroutine.viewmodel
 * class name：FlowRetrofitViewModel
 * desc：FlowRetrofitViewModel
 */
class FlowRetrofitViewModel(app: Application) : AndroidViewModel(app) {
    val articlesBean = MutableLiveData<ArticlesBean>()

    fun searchArticles(page: Int) {
        viewModelScope.launch { // 只能在ViewModel中使用，绑定ViewModel的生命周期，运行在主线程，collect需要协程支持
            flow {
                val result = FlowRetrofitRetrofitClient.articleApi.getArticles(page, 10)
                emit(result)
            }
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    e.printStackTrace()
                }
                .collect { articlesBean.value = it }
        }
    }
}