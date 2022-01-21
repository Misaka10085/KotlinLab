package com.misakanetwork.kotlinlab.coroutine.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.misakanetwork.kotlinlab.coroutine.common.LocalEventBus
import com.misakanetwork.kotlinlab.databinding.FragmentTextBinding
import kotlinx.coroutines.flow.collect

/**
 * Created By：Misaka10085
 * on：2022/1/21
 * package：com.misakanetwork.kotlinlab.coroutine.fragment
 * class name：TextFragment
 * desc：TextFragment
 */
class TextFragment : Fragment() {

    private val mBinding: FragmentTextBinding by lazy {
        FragmentTextBinding.inflate(layoutInflater)
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
                    LocalEventBus.events.collect {
                        mBinding.contentTv.text = it.timestamp.toString()
                    }
                }
                owner.lifecycle.removeObserver(this)
            }
        })
    }
}