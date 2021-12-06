package com.misakanetwork.kotlinlab.jetpack.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.misakanetwork.kotlinlab.R
import com.misakanetwork.kotlinlab.databinding.ItemDataBindingUseBinding
import com.misakanetwork.kotlinlab.jetpack.bean.IdolBean

/**
 * Created By：Misaka10085
 * on：2021/11/27
 * package：com.misakanetwork.kotlinlab.jetpack.adapter
 * class name：MainDataBindingAdapter
 * desc：MainDataBindingAdapter - RecyclerView的DataBinding双向绑定
 */
class MainDataBindingAdapter(private var idolList: MutableList<IdolBean>) :
    RecyclerView.Adapter<MainDataBindingAdapter.MainDataBindingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainDataBindingViewHolder {
        val itemBinding = DataBindingUtil.inflate<ItemDataBindingUseBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_data_binding_use,
            parent,
            false
        )
        return MainDataBindingViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: MainDataBindingViewHolder, position: Int) {
        val idol = idolList[position]
        holder.itemBinding?.idolBean = idol // itemBinding.setIdolBean()
    }

    override fun getItemCount(): Int {
        return idolList.size
    }

    inner class MainDataBindingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemBinding: ItemDataBindingUseBinding? = null

        constructor(itemBinding: ItemDataBindingUseBinding) : this(itemBinding.root) { // itemBinding.getRoot()即可拿到根节点itemView
            this.itemBinding = itemBinding
        }
    }
}