package com.misakanetwork.kotlinlab.coroutine.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.misakanetwork.kotlinlab.R
import com.misakanetwork.kotlinlab.coroutine.viewmodel.VMScopeViewModel
import com.misakanetwork.kotlinlab.databinding.ActivityVmScopeBinding

/**
 * Created By：Misaka10085
 * on：2021/12/6
 * package：com.misakanetwork.kotlinlab.coroutine.activity
 * class name：VMScopeActivity
 * desc：协程(ViewModelScope)+Retrofit+ViewModel+LiveData+DataBinding
 *
 * 在ViewModel中用Repository类去进行网络请求
 */
class VMScopeActivity : AppCompatActivity() {
    private val mainViewModel: VMScopeViewModel by viewModels()

    companion object {
        fun startThis(context: Context) =
            context.startActivity(Intent(context.applicationContext, VMScopeActivity::class.java))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding =
            DataBindingUtil.setContentView<ActivityVmScopeBinding>(this, R.layout.activity_vm_scope)
        binding.viewModel = mainViewModel
        binding.lifecycleOwner = this // 管理当前Activity生命周期
        binding.requestBtn.setOnClickListener {
            mainViewModel.getUserContract(22)
        }
    }
}