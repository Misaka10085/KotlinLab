package com.misakanetwork.kotlinlab.jetpack.widget

import android.content.Context
import android.os.SystemClock
import android.util.AttributeSet
import android.widget.Chronometer
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

/**
 * Created By：Misaka10085
 * on：2021/11/25
 * package：com.misakanetwork.kotlinlab.jetpack
 * class name：LifecycleChronometer
 * desc：jetpack实现绑定生命周期的计时器
 */
class LifecycleChronometer(context: Context, attrs: AttributeSet) : Chronometer(context, attrs),
    LifecycleObserver {
    private var elapsedTime: Long = 0

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private fun startMeter() {
        base = SystemClock.elapsedRealtime() - elapsedTime
        start()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    private fun stopMeter() {
        elapsedTime = SystemClock.elapsedRealtime() - base
        stop()
    }
}