package com.misakanetwork.kotlinlab.coroutine.activity

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.misakanetwork.kotlinlab.R
import com.misakanetwork.kotlinlab.coroutine.api.User
import com.misakanetwork.kotlinlab.coroutine.api.userServiceApi
import kotlinx.android.synthetic.main.activity_main_coroutine.*
import kotlinx.coroutines.*
import kotlin.coroutines.*

/**
 * Created By：Misaka10085
 * on：2021/11/24
 * package：com.misakanetwork.kotlinlab.coroutine
 * class name：MainCoroutineActivity
 * desc：协程
 */
class MainCoroutineActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_coroutine)
        name_tv.text = "Quin"
        compareEachOther()
        suspendAndResume()
        suspendAndBlock()
        createByOriginalApi()
        dispatchers() // 调度器
        coroutineScopes() // 协程作用域
        mainScope()
        viewModelScope() // 协程(ViewModelScope)+Retrofit+ViewModel+LiveData+DataBinding
    }

    @SuppressLint("SetTextI18n,StaticFieldLeak")
    private fun compareEachOther() {
        // AsyncTask way(等一个回调地狱)
        asy_confirm_bt.setOnClickListener {
            object : AsyncTask<Void, Void, User>() {
                override fun onPreExecute() {
                    super.onPreExecute()
                    name_tv.text = "loading..."
                }

                override fun doInBackground(vararg p0: Void?): User? {
                    return userServiceApi.getUserSystem(5).execute().body()
                }

                override fun onPostExecute(user: User?) {
                    name_tv.text = "assignment:${user?.data?.content}"
                }
            }.execute()
        }
        // End
        // coroutine way，异步逻辑同步化
        cor_confirm_bt.setOnClickListener {
            name_tv.text = "loading..."
            // GlobalScope:顶级协程，执行任务时界面销毁仍然存在;CoroutineScope:协程作用域;launch:协程构建器
            GlobalScope.launch(Dispatchers.Main) { // 父协程
                val userLaw =
                    withContext(Dispatchers.IO) { // 协程的任务调度器;启动一个子协程，运行在后台线程，此处可省略，Retrofit会自动检查到suspend自动启用异步线程
                        userServiceApi.getUserLaws(5)
                    }
                name_tv.text = "laws:${userLaw.data.content}"
            }
        }
        // End
    }

    // ----------------------- suspend and resume ----------------------- //
    private fun suspendAndResume() {
        // 执行耗时操作时挂起，执行结束后恢复
        // suspend:用于暂停执行当前协程，并保存所有局部变量
        // resume:用于让已暂停的协程从其暂停处继续执行
        name_tv.text = "loading..."
        suspend_resume_confirm_bt.setOnClickListener {
            GlobalScope.launch(Dispatchers.Main) {
                getUserContract()
            }
        }
    }

    private suspend fun getUserContract() { // 挂起函数
        val userContract = get() // IO结束后恢复并结束get()，恢复到主线程堆栈中getUserContract()也恢复执行，赋值给userContract
        show(userContract)
    }

    private suspend fun get(): User = withContext(Dispatchers.IO) { // 挂起函数，执行时getUserContract()被挂起
        userServiceApi.getUserLaws(5) // 执行异步任务进入Dispatchers.IO，get()被挂起
    }

    private fun show(user: User) {
        name_tv.text = "userContract:${user.data.content}"
    }
    // ----------------------- End ----------------------- //

    // ----------------------- suspend and block ----------------------- //
    private fun suspendAndBlock() {
        suspend_block_confirm_bt.setOnClickListener {
            GlobalScope.launch(Dispatchers.Main) {
                // 挂起 多次点击同时执行多个挂起delay()，即主线程遇见挂起后不受影响依然渲染界面执行逻辑，保存协程挂起点，挂起结束后返回所有结果
                delay(13000) // 挂起函数delay()，延迟？s
                Log.e("suspended", "${Thread.currentThread().name} suspend finished")
            }
            // 阻塞 阻塞了主线程，多次点击顺序执行阻塞，容易导致阻塞主线程的漏帧
//            Thread.sleep(3000)
//            Log.e("blocked", "${Thread.currentThread().name} block finished")
        }
    }
    // ----------------------- End ----------------------- //

    // ----------------------- createByOriginalApi ----------------------- //
    private fun createByOriginalApi() {
        // 基础设施层里面的创建方法，一般用业务框架层
        val continuation: Continuation<Unit> = suspend { // 携程体创建协程 Continuation用于保存协程挂起点，作为协程的上下文
            4
        }.createCoroutine(object : Continuation<Int> {
            override val context: CoroutineContext = EmptyCoroutineContext // 协程上下文

            override fun resumeWith(result: Result<Int>) { // 结果回调
                Log.e("Coroutine simple build", "result:$result")
            }
        })
        continuation.resume(Unit) // 启动协程
    }
    // ----------------------- End ----------------------- //

    // ----------------------- 调度器 ----------------------- //
    private fun dispatchers() {
        // Dispatchers.Main:主线程上运作；调用suspend函数、调用UI函数、更新liveData
        // Dispatchers.IO:专为非主线程上运作，磁盘、网络IO优化；数据全库操作、文件读写、网络处理
        // Dispatchers.Default:专为非主线程上运作，CUP密集型任务优化；数组排序、Json数据解析、处理差异判断
    }
    // ----------------------- End ----------------------- //

    // ----------------------- 协程作用域 ----------------------- //
    private fun coroutineScopes() {
        // GlobalScope:生命周期是process级别的，Activity或Fragment被销毁仍然执行
        // MainScope:Activity中使用，可以在onDestroy中销毁
        // viewModelScope:只能在ViewModel中使用，绑定ViewModel的生命周期
        // lifecycleScope:只能在Activity、Fragment中使用，绑定二者生命周期
    }
    // ----------------------- End ----------------------- //

    // ----------------------- MainScope ----------------------- //
    private val mainScope = MainScope() // MainScope:Activity中使用，可以在onDestroy中销毁
    private fun mainScope() {
        main_scope_bt.setOnClickListener {
            mainScope.launch {
                val user =
//                    withContext(Dispatchers.IO) {
                    userServiceApi.getUserLaws(22)
//                }
                name_tv.text = "mainScope:${user.data.content}"
            }

            // 或使用Activity继承CoroutineScope并委托给MainScope: ..., CoroutineScope by MainScope()
//            launch {
//                ...
//            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mainScope.cancel() // 取消成功会打印日志
//        cancel()
    }
    // ----------------------- End ----------------------- //

    // ----------------------- 协程(ViewModelScope)+Retrofit+ViewModel+LiveData+DataBinding ----------------------- //
    private fun viewModelScope() {
        view_model_scope_bt.setOnClickListener {
            VMScopeActivity.startThis(this)
        }
    }
    // ----------------------- End ----------------------- //
}