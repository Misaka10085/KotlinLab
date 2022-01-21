package com.misakanetwork.kotlinlab.coroutine.common

import kotlinx.coroutines.flow.MutableSharedFlow

/**
 * Created By：Misaka10085
 * on：2022/1/21
 * package：com.misakanetwork.kotlinlab.coroutine.common
 * class name：LocalEventBus
 * desc：实现随机刷新时间的eventBus 具体SharedFlow实现的eventBus建议使用三方库
 */
object LocalEventBus {
    val events = MutableSharedFlow<Event>()

    suspend fun postEvent(event: Event) { // 挂起函数，每次运行保存挂起点，cancel()取消
        events.emit(event)
    }
}

data class Event(val timestamp: Long)