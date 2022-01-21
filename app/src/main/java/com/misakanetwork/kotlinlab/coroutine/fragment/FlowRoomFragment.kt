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
import com.misakanetwork.kotlinlab.coroutine.adapter.SignerAdapter
import com.misakanetwork.kotlinlab.coroutine.viewmodel.FlowRoomViewModel
import com.misakanetwork.kotlinlab.databinding.FragmentFlowRoomBinding
import kotlinx.coroutines.flow.collect


/**
 * Created By：Misaka10085
 * on：2022/1/20
 * package：com.misakanetwork.kotlinlab.coroutine.fragment
 * class name：FlowRoomFragment
 * desc：FlowRoomFragment
 */
class FlowRoomFragment : Fragment() {

    private val viewModel by viewModels<FlowRoomViewModel>()

    private val mBinding: FragmentFlowRoomBinding by lazy {
        FragmentFlowRoomBinding.inflate(layoutInflater)
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
                    addBtn.setOnClickListener {
                        if (idEdt.text.isNullOrEmpty()) return@setOnClickListener
                        viewModel.insert(
                            idEdt.text.toString(),
                            firstNameEdt.text.toString(),
                            lastNameEdt.text.toString()
                        )
                    }
                }
                context?.let {
                    val adapter = SignerAdapter(it)
                    mBinding.mRecyclerView.adapter = adapter
                    lifecycleScope.launchWhenCreated {
                        viewModel.getAll().collect { value ->
                            adapter.setData(value)
                        }
                    }
                }
                owner.lifecycle.removeObserver(this)
            }
        })
    }
}