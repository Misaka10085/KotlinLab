package com.misakanetwork.kotlinlab.jetpack.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.misakanetwork.kotlinlab.R
import com.misakanetwork.kotlinlab.databinding.ActivityMainDataBindingBinding
import com.misakanetwork.kotlinlab.jetpack.activity.viewmodel.UserViewModel
import com.misakanetwork.kotlinlab.jetpack.adapter.MainDataBindingAdapter
import com.misakanetwork.kotlinlab.jetpack.bean.IdolBean
import com.misakanetwork.kotlinlab.jetpack.bean.UserBean
import com.misakanetwork.kotlinlab.jetpack.listener.MainDataBindingHandleListener

/**
 * Created By：Misaka10085
 * on：2021/11/26
 * package：com.misakanetwork.kotlinlab.jetpack.activity
 * class name：MainDataBindingActivity
 * desc：DataBindingUse
 */
class MainDataBindingActivity : AppCompatActivity() {

    companion object {
        fun startThis(context: Context) =
            context.startActivity(Intent(context, MainDataBindingActivity::class.java))
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContent
        val activityMainDataBinding =
            DataBindingUtil.setContentView<ActivityMainDataBindingBinding>( // LayoutNameBinding自动生成
                this,
                R.layout.activity_main_data_binding
            )
        // 控件赋值与点击事件的绑定处理
        val idolBean = IdolBean("Quin", 5)
        activityMainDataBinding.idolBean = idolBean // set要传入的Bean即可，对应的xml中控件需要声明对应的属性接收
        activityMainDataBinding.eventHandler =
            MainDataBindingHandleListener(this) // set绑定xml控件的点击事件，相当于把原生点击事件需要调用的类注册进去
        // 网络图片的加载，xml中定义加载adapter类方法所需的参数
//        activityMainDataBinding.networkImage = "https://profile.csdnimg.cn/6/9/4/2_biushadowblade"
        activityMainDataBinding.networkImage = null
        activityMainDataBinding.smallNetImage = false
        activityMainDataBinding.defaultHolder = R.mipmap.ic_launcher_round

        // 继承BaseObservable来双向绑定EditText，UserBean.userName变化时EditText变化，当输入文本时setUserName
        activityMainDataBinding.userViewModel = UserViewModel(UserBean("Kenny"))

        // RecyclerView的dataBinding使用
        val mData: MutableList<IdolBean> = mutableListOf(
            IdolBean(
                chName = "Quin", enName = "qq", image =
                "https://profile.csdnimg.cn/6/9/4/2_biushadowblade"
            )
        )
        var whileCreate = 0
        do {
            mData.add(mData[0])
            whileCreate++
        } while (whileCreate <= 20)
        val mAdapter = MainDataBindingAdapter(mData)
        activityMainDataBinding.mRecyclerView.layoutManager = LinearLayoutManager(this) // dataBinding持有id去设置属性或setAdapter()
        activityMainDataBinding.mRecyclerView.adapter = mAdapter
        Handler().postDelayed(kotlinx.coroutines.Runnable {
            mData.forEachIndexed { index, item ->
                item.chName = "Kenny"
                item.enName = "sfppp"
                mData[index] = item
            }
            mAdapter.notifyDataSetChanged()
            Toast.makeText(this, "data changed!!", Toast.LENGTH_LONG).show()
        }, 3000)
    }
}