package com.misakanetwork.kotlinlab.jetpack.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.misakanetwork.kotlinlab.R
import com.misakanetwork.kotlinlab.jetpack.activity.viewmodel.MainJetpackViewModel
import kotlinx.android.synthetic.main.fragment_live_data_use.*

/**
 * Created By：Misaka10085
 * on：2021/11/25
 * package：com.misakanetwork.kotlinlab.jetpack.fragment
 * class name：LiveDataUseFragment
 * desc：LiveData+ViewModel的使用
 */
class LiveDataUseFragment : Fragment() {
    private lateinit var viewModel: MainJetpackViewModel

    @SuppressLint("InflateParams")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_live_data_use, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(
            requireActivity(),
            ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
        )[MainJetpackViewModel::class.java]
        viewModel.progress?.observe((requireActivity()), {
            mSeekBar.progress = it
        })
        mSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, p2: Boolean) {
                viewModel.progress?.value = progress
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })
    }
}