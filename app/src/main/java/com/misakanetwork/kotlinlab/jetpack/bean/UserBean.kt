package com.misakanetwork.kotlinlab.jetpack.bean

/**
 * Created By：Misaka10085
 * on：2021/11/26
 * package：com.misakanetwork.kotlinlab.jetpack.bean
 * class name：UserBean
 * desc：UserBean
 */
data class UserBean(
    @kotlin.jvm.JvmField // 供java直接.userName，省略get
    var userName: String?
)