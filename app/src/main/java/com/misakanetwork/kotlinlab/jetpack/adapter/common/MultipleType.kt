package com.misakanetwork.kotlinlab.jetpack.adapter.common

/**
 * Created By：Misaka10085
 * on：2021/4/13
 * package：com.misakanetwork.lib_common.adapter
 * class name：MultipleType
 * desc：CommonAdapter interface
 */
interface MultipleType<T> {
    fun getLayoutId(item: T, position: Int): Int
}