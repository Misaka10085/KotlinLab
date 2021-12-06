package com.misakanetwork.kotlinlab.coroutine.repository

import com.misakanetwork.kotlinlab.coroutine.api.User
import com.misakanetwork.kotlinlab.coroutine.api.userServiceApi

/**
 * Created By：Misaka10085
 * on：2021/12/6
 * package：com.misakanetwork.kotlinlab.coroutine.repository
 * class name：UserContractRepository
 * desc：UserContractRepository，ViewModel持有它进行网络请求
 */
class UserContractRepository {
    suspend fun getUserContract(id: Int): User {
        return userServiceApi.getUserLaws(id)
    }
}