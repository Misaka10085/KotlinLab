package com.misakanetwork.kotlinlab.jetpack.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.misakanetwork.kotlinlab.R
import com.misakanetwork.kotlinlab.jetpack.bean.ArticleDataBean

/**
 * Created By：Misaka10085
 * on：2021/12/3
 * package：com.misakanetwork.kotlinlab.jetpack.adapter
 * class name：ArticlesPagedListAdapter
 * desc：ArticlesPagedListAdapter
 */
class ArticlesPagedListAdapter(private val context: Context) :
    PagedListAdapter<ArticleDataBean, ArticlesPagedListAdapter.ArticleViewHolder>(diffCallback) {
    // 重写带diffCallback的构造方法 DiffUtil:只更新需要更新的元素，而不是整个数据源
    // Java:需要在ArticleDataBean重写equals方法、hashCode方法
    companion object {
        private val diffCallback: DiffUtil.ItemCallback<ArticleDataBean> =
            object : DiffUtil.ItemCallback<ArticleDataBean>() {
                override fun areItemsTheSame(
                    oldItem: ArticleDataBean,
                    newItem: ArticleDataBean
                ): Boolean { // 判断对象是否和刷新前的是同一对象
                    return oldItem === newItem // 这里如果是Java，则为==
                }

                override fun areContentsTheSame( // 判断数据是否一样,Java:equals
                    oldItem: ArticleDataBean,
                    newItem: ArticleDataBean
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_paging_use, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val bean = getItem(position) ?: return
        holder.titleTv.text = bean.title
        holder.contentTv.text = bean.desc
    }

    inner class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.mImageView)
        val titleTv: TextView = itemView.findViewById(R.id.title_tv)
        val contentTv: TextView = itemView.findViewById(R.id.content_tv)
    }
}