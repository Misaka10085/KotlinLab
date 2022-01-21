package com.misakanetwork.kotlinlab.coroutine.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.misakanetwork.kotlinlab.coroutine.db.SignerInfo
import com.misakanetwork.kotlinlab.databinding.ItemSignerBinding

/**
 * Created By：Misaka10085
 * on：2022/1/20
 * package：com.misakanetwork.kotlinlab.coroutine.adapter
 * class name：SignerAdapter
 * desc：SignerAdapter
 */
class SignerAdapter(private val context: Context) : RecyclerView.Adapter<BindingViewHolder>() {
    private val data = ArrayList<SignerInfo>()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<SignerInfo>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder {
        val binding =
            ItemSignerBinding.inflate(LayoutInflater.from(context), parent, false) // xml文件转换为对象
        return BindingViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {
        val item = data[position]
        val binding = holder.binding as ItemSignerBinding
        binding.textTv.text = "${item.uid},${item.firstName},${item.lastName}"
    }

    override fun getItemCount(): Int = data.size
}