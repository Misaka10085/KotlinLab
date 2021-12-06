package com.misakanetwork.kotlinlab.jetpack.service

import android.util.Log
import androidx.lifecycle.LifecycleService

/**
 * Created By：Misaka10085
 * on：2021/11/25
 * package：com.misakanetwork.kotlinlab.jetpack
 * class name：JetpackLifecycleService
 * desc：Jetpack实现Service
 */
class JetpackLifecycleService : LifecycleService() {

    init {
        Log.e("Quin service", "JetpackLifecycleService")
        val observer = LocationServiceObserver(this) // 为Service创建继承LifecycleObserver的observer
        lifecycle.addObserver(observer) // 监听自定义service的observer
    }
}