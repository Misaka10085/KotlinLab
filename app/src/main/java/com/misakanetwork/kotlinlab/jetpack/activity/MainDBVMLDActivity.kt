package com.misakanetwork.kotlinlab.jetpack.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.misakanetwork.kotlinlab.R
import com.misakanetwork.kotlinlab.databinding.ActivityMainDbvmldBinding
import com.misakanetwork.kotlinlab.jetpack.activity.viewmodel.MainDBVMLDViewModel

/**
 * Created By：Misaka10085
 * on：2021/11/27
 * package：com.misakanetwork.kotlinlab.jetpack.activity
 * class name：MainDBVMLDActivity
 * desc：DataBinding+ViewModel+LiveData 计分板加减
 * 业务逻辑由ViewModel+LiveData类实现，DataBinding通过JetpackLifecycle感知该Activity生命周期
 */
class MainDBVMLDActivity : AppCompatActivity() {

    companion object {
        fun startThis(context: Context) = context.startActivity(
            Intent(
                context.applicationContext,
                MainDBVMLDActivity::class.java
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val activityMainDBVMLDBinding = DataBindingUtil.setContentView<ActivityMainDbvmldBinding>(
            this,
            R.layout.activity_main_dbvmld
        )
        activityMainDBVMLDBinding.viewModel =
            ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(application))[MainDBVMLDViewModel::class.java]
        // DataBinding通过JetpackLifecycle感知该Activity生命周期
        activityMainDBVMLDBinding.lifecycleOwner = this
    }
}