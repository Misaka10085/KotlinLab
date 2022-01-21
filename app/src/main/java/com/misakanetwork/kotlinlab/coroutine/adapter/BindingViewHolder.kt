package com.misakanetwork.kotlinlab.coroutine.adapter

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 * Created By：Misaka10085
 * on：2022/1/20
 * package：com.misakanetwork.kotlinlab.coroutine.adapter
 * class name：BindingViewHolder
 * desc：BindingViewHolder 传入ViewBinding，返回对应ViewHolder
 */
class BindingViewHolder(val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {
}