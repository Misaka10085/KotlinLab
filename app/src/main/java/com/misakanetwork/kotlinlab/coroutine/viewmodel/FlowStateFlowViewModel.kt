package com.misakanetwork.kotlinlab.coroutine.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Created By：Misaka10085
 * on：2022/1/21
 * package：com.misakanetwork.kotlinlab.coroutine.viewmodel
 * class name：FlowStateFlowViewModel
 * desc：FlowStateFlowViewModel
 */
class FlowStateFlowViewModel : ViewModel() {
    val number = MutableStateFlow(0)

    fun increment() {
        number.value++
    }

    fun decrement() {
        number.value--
    }
}