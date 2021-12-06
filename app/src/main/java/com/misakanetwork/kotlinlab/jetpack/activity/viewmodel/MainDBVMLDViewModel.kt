package com.misakanetwork.kotlinlab.jetpack.activity.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * Created By：Misaka10085
 * on：2021/11/27
 * package：com.misakanetwork.kotlinlab.jetpack.activity.viewmodel
 * class name：MainDBVMLDViewModel
 * desc：DataBinding+ViewModel+LiveData 's ViewModel
 */
class MainDBVMLDViewModel : ViewModel() {
    var aTeamScore: MutableLiveData<Int>? = null
        get() = field ?: let {
            field = MutableLiveData()
            field!!.value = 0
            field
        }

    var bTeamScore: MutableLiveData<Int>? = null
        get() = field ?: let {
            field = MutableLiveData()
            field!!.value = 0
            field
        }

    var aLast: Int = 0
    var bLast: Int = 0

    fun aTeamAdd(num: Int) {
        saveLastScore()
        aTeamScore?.value = aTeamScore?.value?.plus(num)
    }

    fun bTeamAdd(num: Int) {
        saveLastScore()
        bTeamScore?.value = bTeamScore?.value?.plus(num)
    }

    private fun saveLastScore() {
        // 记录上一分
        aLast = aTeamScore?.value ?: aLast
        bLast = bTeamScore?.value ?: bLast
    }

    fun undo() {
        aTeamScore?.value = aLast
        bTeamScore?.value = bLast
    }

    fun reset() {
        aTeamScore?.value = 0
        bTeamScore?.value = 0
    }
}