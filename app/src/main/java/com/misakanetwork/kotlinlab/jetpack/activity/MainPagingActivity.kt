package com.misakanetwork.kotlinlab.jetpack.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.misakanetwork.kotlinlab.R
import com.misakanetwork.kotlinlab.jetpack.adapter.ArticlesPagedListAdapter
import com.misakanetwork.kotlinlab.jetpack.paging.ArticlesViewModel
import kotlinx.android.synthetic.main.activity_main_paging.*

/**
 * Created By：Misaka10085
 * on：2021/12/2
 * package：com.misakanetwork.kotlinlab.jetpack.activity
 * class name：MainPagingActivity
 * desc：Paging列表分页
 *          引入paging依赖
 */
class MainPagingActivity : AppCompatActivity(R.layout.activity_main_paging) {
    private lateinit var articlesViewModel: ArticlesViewModel

    companion object {
        fun startThis(context: Context) {
            context.startActivity(
                Intent(
                    context.applicationContext,
                    MainPagingActivity::class.java
                )
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = ArticlesPagedListAdapter(this)
        mRecyclerView.adapter = adapter
        articlesViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(application)
        )[ArticlesViewModel::class.java]
        articlesViewModel.articlePagedList.observe(this) {
            adapter.submitList(it)
        }

//        mSwipeRefreshLayout.setOnRefreshListener {
//            articlesViewModel.refresh()
//            mSwipeRefreshLayout.isRefreshing = false
//        }
    }
}