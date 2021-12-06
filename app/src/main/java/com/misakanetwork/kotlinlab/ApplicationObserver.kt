package com.misakanetwork.kotlinlab

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

/**
 * Created By：Misaka10085
 * on：2021/11/25
 * package：com.misakanetwork.kotlinlab
 * class name：ApplicationObserver
 * desc：processLifecycleOwner-监听程序生命周期
 * 针对整个程序的生命周期的监听，即Lifecycle.Event.ON_CREATE、Lifecycle.Event.ON_DESTROY只可能执行一次
 */
@SuppressLint("LongLogTag")
class ApplicationObserver : LifecycleObserver {
    private val TAG = "processLifecycleOwner-ApplicationObserver"

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    private fun onCreate() = Log.e(TAG, "Lifecycle.Event.ON_CREATE")

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private fun onStart() = Log.e(TAG, "Lifecycle.Event.ON_START")

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private fun onResume() = Log.e(TAG, "Lifecycle.Event.ON_RESUME")

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    private fun onPause() = Log.e(TAG, "Lifecycle.Event.ON_PAUSE")

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private fun onStop() = Log.e(TAG, "Lifecycle.Event.ON_STOP")

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun onDestroy() = Log.e(TAG, "Lifecycle.Event.ON_DESTROY")
}