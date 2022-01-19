package com.misakanetwork.kotlinlab

import android.annotation.SuppressLint
import android.util.Log
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlin.coroutines.CoroutineContext

/**
 * Created By：Misaka10085
 * on：2021/12/10
 * package：com.misakanetwork.kotlinlab
 * class name：GlobalCoroutineExceptionHandler
 * desc：全局异常处理器
 */
class GlobalCoroutineExceptionHandler : CoroutineExceptionHandler {
    override val key = CoroutineExceptionHandler

    @SuppressLint("LongLogTag")
    override fun handleException(context: CoroutineContext, exception: Throwable) {
        Log.e("GlobalCoroutineExceptionHandler", "Unhandled coroutine Exception: $exception")
    }
}