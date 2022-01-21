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
import androidx.lifecycle.lifecycleScope
import com.misakanetwork.kotlinlab.coroutine.viewmodel.FlowStateFlowViewModel
import com.misakanetwork.kotlinlab.databinding.FragmentFlowStateFlowBinding
import kotlinx.coroutines.flow.collect

/**
 * Created By：Misaka10085
 * on：2022/1/21
 * package：com.misakanetwork.kotlinlab.coroutine.fragment
 * class name：FlowStateFlowFragment
 * desc：FlowStateFlowFragment
 * StateFlow,热流，在垃圾回收之前都存在于内存中活跃，作用类似LiveData，最后Collect收集即可及时更新UI
 */
class FlowStateFlowFragment : Fragment() {
    private val viewModel by viewModels<FlowStateFlowViewModel>()

    private val mBinding: FragmentFlowStateFlowBinding by lazy {
        FragmentFlowStateFlowBinding.inflate(layoutInflater)
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
                    plusBtn.setOnClickListener {
                        viewModel.increment()
                    }
                    minusBtn.setOnClickListener {
                        viewModel.decrement()
                    }
                }
                lifecycleScope.launchWhenCreated {
                    viewModel.number.collect { value ->
                        mBinding.numTv.text = value.toString()
                    }
                }
                owner.lifecycle.removeObserver(this)
            }
        })
    }
}