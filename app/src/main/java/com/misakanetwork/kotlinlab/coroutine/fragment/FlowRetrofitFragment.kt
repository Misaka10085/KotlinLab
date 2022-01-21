package com.misakanetwork.kotlinlab.coroutine.fragment

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.misakanetwork.kotlinlab.coroutine.adapter.ArticleAdapter
import com.misakanetwork.kotlinlab.coroutine.viewmodel.FlowRetrofitViewModel
import com.misakanetwork.kotlinlab.databinding.FragmentFlowRetrofitBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ClosedSendChannelException
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.onClosed
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect

/**
 * Created By：Misaka10085
 * on：2022/1/20
 * package：com.misakanetwork.kotlinlab.coroutine.fragment
 * class name：FlowRetrofitFragment
 * desc：FlowRetrofitFragment
 * 在输入框中输入内容发送到Flow，由ViewModel collect后使用Flow发送内容并请求接口获取结果,最后ViewModel collect后UI observe更新
 */
@ExperimentalCoroutinesApi
class FlowRetrofitFragment : Fragment() {
    private val viewModel by viewModels<FlowRetrofitViewModel>()

    private val mBinding: FragmentFlowRetrofitBinding by lazy {
        FragmentFlowRetrofitBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return mBinding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onCreate(owner: LifecycleOwner) {
                super.onCreate(owner)
                lifecycleScope.launchWhenCreated {
                    // 解决下面的collect嵌套情况，需要ViewModel引用LiveData机制，将EditText的collect操作交由ViewModel，接口返回的数据使用LiveData observe机制
//                    mBinding.searchEdt.textWatcherFlow().collect {
//                        Log.e("Flow Retrofit", "collect keywords:$it")
//                        if (it.isNullOrEmpty()) return@collect
//                        viewModel.searchArticles(it.toInt()).collect {
//
//                        }
//                    }
                    mBinding.searchEdt.textWatcherFlow().collect {
                        if (it.isEmpty()) return@collect
                        viewModel.searchArticles(it.toInt())
                    }
                }
                owner.lifecycle.removeObserver(this)
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.let {
            val adapter = ArticleAdapter(it)
            mBinding.mRecyclerView.adapter = adapter
            viewModel.articlesBean.observe(viewLifecycleOwner, { articles ->
                // viewLifecycleOwner即Fragment（因为ViewModel在Fragment中创建），绑定Fragment生命周期
                adapter.setData(articles.data.datas)
            })
        }
    }

    /**
     * 使用扩展函数+Flow，由callbackFlow返回输入框文字
     */
    private fun TextView.textWatcherFlow(): Flow<String> = callbackFlow {
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
//                offer(p0.toString())
                channel.trySend(p0.toString())
                    .onClosed {
                        throw it ?: ClosedSendChannelException("Channel was closed normally")
                    }
                    .isSuccess
            }
        }
        addTextChangedListener(textWatcher)
        awaitClose { removeTextChangedListener(textWatcher) }
    }
}