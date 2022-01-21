package com.misakanetwork.kotlinlab.coroutine.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.misakanetwork.kotlinlab.databinding.ItemArticleBinding
import com.misakanetwork.kotlinlab.jetpack.bean.ArticleDataBean

/**
 * Created By：Misaka10085
 * on：2022/1/20
 * package：com.misakanetwork.kotlinlab.coroutine.adapter
 * class name：ArticleAdapter
 * desc：ArticleAdapter
 */
class ArticleAdapter(private val context: Context) : RecyclerView.Adapter<BindingViewHolder>() {
    private val data = ArrayList<ArticleDataBean>()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<ArticleDataBean>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder {
        val binding = ItemArticleBinding.inflate(LayoutInflater.from(context), parent, false)
        return BindingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {
        val item = data[position]
        val binding = holder.binding as ItemArticleBinding
        binding.titleTv.text = item.title
        binding.contentTv.text = item.desc
    }

    override fun getItemCount(): Int = data.size
}