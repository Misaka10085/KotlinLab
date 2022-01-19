package com.misakanetwork.kotlinlab.coroutine.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import com.misakanetwork.kotlinlab.R
import com.misakanetwork.kotlinlab.databinding.FragmentFlowAppHomeBinding

/**
 * Created By：Misaka10085
 * on：2022/1/19
 * package：com.misakanetwork.kotlinlab.coroutine.fragment
 * class name：FlowAppHomeFragment
 * desc：FlowAppHomeFragment
 */
class FlowAppHomeFragment : Fragment() {
    private val mBinding: FragmentFlowAppHomeBinding by lazy {
        FragmentFlowAppHomeBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return mBinding.root
    }

    override fun onAttach(context: Context) { // 代替onActivityCreated，需及时remove observer
        super.onAttach(context)
        requireActivity().lifecycle.addObserver(object :DefaultLifecycleObserver{
            override fun onCreate(owner: LifecycleOwner) {
                super.onCreate(owner)
                mBinding.flowDownloadBtn.setOnClickListener {
                    findNavController().navigate(R.id.action_flowAppHomeFragment_to_flowDownloadFragment)
                }
                owner.lifecycle.removeObserver(this)
            }
        })
    }
}