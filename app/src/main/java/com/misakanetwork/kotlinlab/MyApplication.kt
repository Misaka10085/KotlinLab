package com.misakanetwork.kotlinlab

import android.app.Application
import kotlin.properties.Delegates

/**
 * Created By：Misaka10085
 * on：2021/11/29
 * package：com.misakanetwork.kotlinlab
 * class name：Application
 * desc：Application 提供全局Context
 */
class MyApplication : Application() {

    companion object {
        var instance: MyApplication by Delegates.notNull()

        fun instance() = instance
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}