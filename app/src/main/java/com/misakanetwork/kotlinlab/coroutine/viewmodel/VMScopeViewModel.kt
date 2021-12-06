package com.misakanetwork.kotlinlab.coroutine.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.misakanetwork.kotlinlab.coroutine.api.User
import com.misakanetwork.kotlinlab.coroutine.repository.UserContractRepository
import kotlinx.coroutines.launch

/**
 * Created By：Misaka10085
 * on：2021/12/6
 * package：com.misakanetwork.kotlinlab.coroutine.viewmodel
 * class name：VMScopeViewModel
 * desc：ViewModel
 */
class VMScopeViewModel : ViewModel() {
    val userContractLiveData = MutableLiveData<User>()

    private val userContractRepository by lazy { UserContractRepository() }

    fun getUserContract(id: Int) {
        viewModelScope.launch {
            userContractLiveData.value = userContractRepository.getUserContract(id)
        }
    }
}