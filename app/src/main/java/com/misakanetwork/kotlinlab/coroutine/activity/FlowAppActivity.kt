package com.misakanetwork.kotlinlab.coroutine.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.misakanetwork.kotlinlab.databinding.ActivityFlowAppBinding

/**
 * Created By：Misaka10085
 * on：2022/1/19
 * package：com.misakanetwork.kotlinlab.coroutine.activity
 * class name：FlowAppActivity
 * desc：flow+协程+ViewBinding+LiveData/StateFlow+SharedFlow+Navigation+Retrofit+Room
 */
class FlowAppActivity : AppCompatActivity() {
    private val mBinding: ActivityFlowAppBinding by lazy {
        ActivityFlowAppBinding.inflate(layoutInflater)
    }

    companion object {
        fun startThis(context: Context) {
            context.startActivity(Intent(context.applicationContext, FlowAppActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
    }
}