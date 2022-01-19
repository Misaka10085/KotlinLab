package com.misakanetwork.kotlinlab.coroutine.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.misakanetwork.kotlinlab.coroutine.download.DownloadManager
import com.misakanetwork.kotlinlab.coroutine.download.DownloadStatus
import com.misakanetwork.kotlinlab.databinding.FragmentFlowDownloadBinding
import kotlinx.coroutines.flow.collect
import java.io.File

/**
 * Created By：Misaka10085
 * on：2022/1/19
 * package：com.misakanetwork.kotlinlab.coroutine.fragment
 * class name：FlowDownloadFragment
 * desc：FlowDownloadFragment
 * 通过自定义的DownloadManager Flow对文件读写操作进行文件下载
 */
class FlowDownloadFragment : Fragment() {
    private val mBinding: FragmentFlowDownloadBinding by lazy {
        FragmentFlowDownloadBinding.inflate(layoutInflater)
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
                lifecycleScope.launchWhenCreated { // collect需要在协程执行,且Fragment关闭后需要协程取消的机制自动关闭上游的下载任务以防止内存泄漏
                    context?.apply {
                        val file = File(getExternalFilesDir(null)?.path, "teacher.apk")
                        DownloadManager.download(
                            "https://qyss.oss-cn-hangzhou.aliyuncs.com/app/android/teacher/qyzhjy_qyss_teacher.apk",
                            file
                        ).collect { status ->
                            when (status) {
                                is DownloadStatus.Progress -> {
                                    mBinding.apply {
                                        mProgressbar.progress = status.value
                                        progressTv.text = "${status.value}"
                                    }
                                }
                                is DownloadStatus.Error -> {
                                    Toast.makeText(context, "下载错误", Toast.LENGTH_SHORT).show()
                                }
                                is DownloadStatus.Done -> {
                                    mBinding.apply {
                                        mProgressbar.progress = 100
                                        progressTv.text = "100%"
                                    }
                                    Toast.makeText(context, "下载完成", Toast.LENGTH_SHORT).show()
                                }
                                else -> {
                                    Log.e("downloadManager", "下载失败")
                                }
                            }
                        }
                    }
                }
                owner.lifecycle.removeObserver(this)
            }
        })
    }
}