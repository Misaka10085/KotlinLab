package com.misakanetwork.kotlinlab.coroutine.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.misakanetwork.kotlinlab.coroutine.common.Event
import com.misakanetwork.kotlinlab.coroutine.common.LocalEventBus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * Created By：Misaka10085
 * on：2022/1/21
 * package：com.misakanetwork.kotlinlab.coroutine.viewmodel
 * class name：FlowSharedFlowViewModel
 * desc：FlowSharedFlowViewModel
 */
class FlowSharedFlowViewModel : ViewModel() {
    private lateinit var job: Job

    fun startRefresh() {
        job = viewModelScope.launch(Dispatchers.IO) {
            while (true) {
                LocalEventBus.postEvent(Event(System.currentTimeMillis()))
            }
        }
    }

    fun stopRefresh() {
        job.cancel()
    }
}