package com.misakanetwork.kotlinlab.jetpack.listener

import android.content.Context
import android.view.View
import android.widget.Toast

/**
 * Created By：Misaka10085
 * on：2021/11/26
 * package：com.misakanetwork.kotlinlab.jetpack.listener
 * class name：MainDataBindingHandleListener
 * desc：ManDataBindingActivity点击事件处理
 */
class MainDataBindingHandleListener(private val context: Context) {

    fun onLikeClick(view: View) {
        Toast.makeText(context, "喜欢", Toast.LENGTH_LONG).show()
    }
}