package com.misakanetwork.kotlinlab.coroutine.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.misakanetwork.kotlinlab.coroutine.viewmodel.FlowSharedFlowViewModel
import com.misakanetwork.kotlinlab.databinding.FragmentFlowSharedFlowBinding

/**
 * Created By：Misaka10085
 * on：2022/1/21
 * package：com.misakanetwork.kotlinlab.coroutine.fragment
 * class name：FlowSharedFlowFragment
 * desc：FlowSharedFlowFragment
 * 热流，在垃圾回收之前都存在于内存中活跃,会向所有collect方发送数据(类似BroadcastChannel)
 */
class FlowSharedFlowFragment : Fragment() {
    private val viewModel by viewModels<FlowSharedFlowViewModel>()

    private val mBinding: FragmentFlowSharedFlowBinding by lazy {
        FragmentFlowSharedFlowBinding.inflate(layoutInflater)
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
                mBinding.apply {
                    startBtn.setOnClickListener {
                        viewModel.startRefresh()
                    }
                    stopBtn.setOnClickListener {
                        viewModel.stopRefresh()
                    }
                }
                owner.lifecycle.removeObserver(this)
            }
        })
    }
}