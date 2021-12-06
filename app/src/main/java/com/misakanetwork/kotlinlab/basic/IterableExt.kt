package com.misakanetwork.kotlinlab.basic

/**
 * Created By：Misaka10085
 * on：2021/11/23
 * package：com.misakanetwork.kotlinlab
 * class name：IterableExt
 * desc：扩展文件
 */
fun <T> Iterable<T>.randomTake(): T = this.shuffled().first()