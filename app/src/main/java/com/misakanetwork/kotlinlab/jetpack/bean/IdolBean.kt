package com.misakanetwork.kotlinlab.jetpack.bean

import java.io.Serializable

/**
 * Created By：Misaka10085
 * on：2021/11/26
 * package：com.misakanetwork.kotlinlab.jetpack.bean
 * class name：IdolBean
 * desc：数据Bean
 */
data class IdolBean(
    val name: String? = null,
    val star: Int = 0,
    var chName: String? = null,
    var enName: String? = null,
    val image: String? = null
) : Serializable