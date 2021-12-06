package com.misakanetwork.kotlinlab.jetpack.utils

/**
 * Created By：Misaka10085
 * on：2021/11/26
 * package：com.misakanetwork.kotlinlab.jetpack.utils
 * class name：StarUtils
 * desc：StarUtils
 */
object StarUtils {

    @JvmStatic
    fun getStar(star: Int): String {
        return when (star) {
            1 -> "一星"
            2 -> "二星"
            3 -> "三星"
            4 -> "四星"
            5 -> "五星"
            else -> ""
        }
    }
}