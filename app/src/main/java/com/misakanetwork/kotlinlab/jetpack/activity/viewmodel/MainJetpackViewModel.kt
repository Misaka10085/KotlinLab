package com.misakanetwork.kotlinlab.jetpack.activity.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * Created By：Misaka10085
 * on：2021/11/25
 * package：com.misakanetwork.kotlinlab.jetpack.activity.viewmodel
 * class name：MainJetpackViewModel
 * desc：MainJetpackViewModel，单独使用LiveData保持View与bean之间的通信
 */
class MainJetpackViewModel : ViewModel() {

    var number: Int = 0
    var currentSecond: MutableLiveData<Int>? = null // 定义一个LiveData Integer的变量
        get() { // get方法空判断
            field = field ?: let {
                field = MutableLiveData()
                field!!.value = 0
                field
            }
            return field
        }
    var progress: MutableLiveData<Int>? = null
        get() {
            field = field ?: let {
                field = MutableLiveData()
                field!!.value = 0
                field
            }
            return field
        }
}

//class MainJetpackViewModel(application: Application) : AndroidViewModel(application) { // 需要context时继承AndroidViewModel
//
//    var number: Int = 0
//}