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
import kotlin.system.measureTimeMillis

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
        coroutineLaunchAndAsync() // 网络请求等待、并发原理 runBlocking join await async组合并发
        startMode() // 启动模式
        scope() // 协程作用域与构建器
        coroutineCancel()
        use() // 自动释放资源
        dealWithTimeout()
        context() // 协程上下文
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

            // 或使用Activity继承CoroutineScope并委托给MainScope: ..., CoroutineScope by MainScope(){
//                ...
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

    // ----------------------- coroutine async launch ----------------------- //
    @SuppressLint("LongLogTag")
    private fun coroutineLaunchAndAsync() =
        runBlocking { // 将主线程变为主协程，即启动一个新的协程，但仍然在当前（主）线程中 会阻塞线程来等待
            val job1 = launch { // 子协程，返回一个Job
                delay(200)
                Log.e("coroutineAsyncAndDelay", "launch:Job1 finished")
            }
            val job2 = async { // 子协程，返回一个Job子类 Deferred,可以得到返回结果
                delay(200)
                Log.e("coroutineAsyncAndDelay", "async:Job2 finished")
                "job2 result"
            }
            // runBlocking会等待子协程全部执行完毕后关闭
            Log.e("coroutineAsyncAndDelay async", job2.await())

            launchJoin() // launch方式协程等待
            asyncAwait() // async方式协程等待
        }

    private fun launchJoin() = runBlocking {
        val job1 = launch {
            delay(2000)
            Log.e("launchJoin", "one")
        }
        job1.join() // job2和job3会在job1执行完毕后才执行
        val job2 = launch {
            delay(200)
            Log.e("launch", "two")
        }
        val job3 = launch {
            delay(200)
            Log.e("launch", "three")
        }
    }

    private fun asyncAwait() = runBlocking {
        val job1 = async {
            delay(2000)
            Log.e("asyncAwait", "one")
        }
        job1.await() // job2和job3会在job1执行完毕后才执行，await()也会持有协程的返回值
        val job2 = async {
            delay(200)
            Log.e("async", "two")
        }
        val job3 = async {
            delay(200)
            Log.e("async", "three")
        }

        // async结构化组合并发
        val completeTime = measureTimeMillis { // 时间花费统计
            // 同步执行
//            val one = doOne()
//            val two = doTwo()
            // async异步并发
            val one = async { doOne() }
            val two = async { doTwo() }
//            Log.e("async并发", "result is ${one + two}")
            Log.e("async并发", "result is ${one.await() + two.await()}")
        }
        Log.e("async并发", "completed in $completeTime")
    }

    private suspend fun doOne(): Int {
        delay(1000)
        return 14
    }

    private suspend fun doTwo(): Int {
        delay(1000)
        return 25
    }
    // ----------------------- End ----------------------- //

    // ----------------------- 协程启动模式 ----------------------- //
    private fun startMode() = runBlocking {
        // CoroutineStart.DEFAULT:创建该协程后立即调度,但若主线程在执行其它任务的话就需要等待主线程执行完成后才执行协程任务(即调度但不立即执行);
        // cancel()的时候（包括在调度前）立即就进入取消的响应状态后取消
        val job = launch(start = CoroutineStart.DEFAULT) {
            delay(10000) // 挂起该子协程，主线程会去执行下面其它的任务
            Log.e("startMode", "CoroutineStart.DEFAULT:Job finished")
        }
        delay(1000)
        job.cancel() // 挂起一秒立即关闭子协程

        // CoroutineStart.ATOMIC:创建该协程后立即调度，同样调度但不立即执行;
        // cancel()只在挂起点之后才有效
        val job2 = launch(start = CoroutineStart.ATOMIC) {
            Log.e("startMode", "running0")
            Log.e("startMode", "running1")
            cancel() // 在挂起点出现之前不会取消成功，挂起点出现后执行
            Log.e("startMode", "running2")
            Log.e("startMode", "running3")
            delay(3000) // 挂起该子协程后，可以取消
            Log.e("startMode", "CoroutineStart.ATOMIC:Job finished")
        }

        // CoroutineStart.LAZY:惰性创建协程，被需要时才创建开始调度，start、join、await等函数调用后才会执行;
        // 任何时候(包括调度前)都可取消
        val job3 = async(start = CoroutineStart.LAZY) {
            22
        }
        delay(3000) // 模拟执行其它任务
        job3.start() // 调用start、join、await等的时候才会调度

        // CoroutineStart.UNDISPATCHED:在哪个线程中被创建就在哪个线程立即执行
//        al job4 = async(context = Dispatchers.IO, start = CoroutineStart.DEFAULT) { // 子线程中调度然后执行
        val job4 = async(context = Dispatchers.IO, start = CoroutineStart.UNDISPATCHED) {
            Log.e("startMode", "thread:${Thread.currentThread().name}") // 当前线程（runBlocking主线程）中立即执行
        }
    }
    // ----------------------- End ----------------------- //

    // ----------------------- 协程作用域构建器 ----------------------- //
    private fun scope() = runBlocking { // 普通函数，阻塞当前线程来等待
        coroutineScope { // 是一个挂起函数.一个协程失败了，作用域内其他所有协程都会取消
            val job1 = launch {
                delay(400)
                Log.e("coroutineScope", "job1 finished")
            }
            val job2 = launch {
                delay(200)
                Log.e("coroutineScope", "job2 finished")
//                throw IllegalArgumentException() // job1会被取消
            }
        }

        supervisorScope {
            val job3 = launch {
                delay(400)
                Log.e("supervisorScope", "job3 finished")
            }
            val job4 = launch {
                delay(200)
                Log.e("supervisorScope", "job4 finished")
//                throw IllegalArgumentException() // job3不会受影响
            }
        }
    }
    // ----------------------- End ----------------------- //

    // ----------------------- coroutineCancel ----------------------- //
    private fun coroutineCancel() = runBlocking {
        val scope = CoroutineScope(Dispatchers.Default)
        val job1 = scope.launch {
            delay(1000)
            Log.e("coroutineCancel", "job1")
        }
        val job2 = scope.launch {
            delay(1000)
            Log.e("coroutineCancel", "job2")
        }
//        scope.cancel()
        job1.cancel() // 不会影响job2

        val job3 = GlobalScope.launch {
            try {
                delay(1000)
                Log.e("coroutineCancel", "job3")
            } catch (e: Exception) {
                Log.e("coroutineCancel", "catch when cancel job3: ${e.message}")
            }
        }
        job3.cancel()
//        job3.cancel(CancellationException("取消"))
//        job3.cancelAndJoin() // 等待取消完成

        // CPU密集型任务运行时不能被取消
        val startTime = System.currentTimeMillis()
        val jobTime = launch(Dispatchers.Default) {
            var nextPrintTime = startTime
            var i = 0
            while (i < 5) {
//            while (i < 5 && isActive) { // 使用isActive在循环中进行判断查询Job是否处于活跃状态 cancel才有效
//                ensureActive() // 使用ensureActive，job非活跃状态则取消并可捕获抛出异常
//                yield() // 检查当前所在协程状态，协程取消则取消当前任务并抛出可捕获异常。yield可以在高密度任务中出让线程的执行权让其它协程也有运行机会
                if (System.currentTimeMillis() >= nextPrintTime) {
                    Log.e("系统密集型任务", "job:Sleeping ${i++}...")
                    nextPrintTime += 500
                }
            }
        }
        delay(1300)
        Log.e("系统密集型任务", "now cancel")
        jobTime.cancelAndJoin() // 无效
        Log.e("系统密集型任务", "canceled")

        // finally中释放资源
        val jobRepeat = launch {
            try {
                repeat(1000) {
                    Log.e("coroutineCancel", "repeat sleaping...")
                    delay(500)
                }
            } finally {
//                delay(1000) // 耗时操作模拟  finally中不可执行成功
//                Log.e("coroutineCancel", "finally")
                withContext(NonCancellable) { // 允许耗时操作任务，常驻任务也可使用该函数
                    Log.e("coroutineCancel", "run something in finally")
                    delay(1000)
                    Log.e("coroutineCancel", "finally completed")
                }
            }
        }
        delay(1300)
        Log.e("coroutineCancel", "start cancel it and release resource")
        jobRepeat.cancelAndJoin()
        Log.e("coroutineCancel", "cancel it and release resource completed")
    }
    // ----------------------- End ----------------------- //

    // ----------------------- use ----------------------- //
    private fun use() = runBlocking {
//        val br = BufferedReader(FileReader("D:\\I have a dream.txt"))
//        with(br) {
//            var line: String?
//            try {
//                while (true) {
//                    line = readLine() ?: break
//                    Log.e("use", "origin: $line")
//                }
//            } finally {
//                close()
//            }
//        }

        // use:执行完毕后会自动释放资源
//        BufferedReader(FileReader("D:\\I have a dream.txt")).use {
//            var line: String?
//            while (true) {
//                line = it.readLine() ?: break
//                Log.e("use", line)
//            }
//        }
    }
    // ----------------------- End ----------------------- //

    // ----------------------- 超时任务的处理 ----------------------- //
    private fun dealWithTimeout() = runBlocking {
        try {
            withTimeout(1300) { // 超过指定时间就退出并抛出异常
                repeat(1000) { rep ->
                    Log.e("dealWithTimeout", "repeating: $rep ...")
                    delay(500L)
                }
            }
        } catch (e: Exception) {
            Log.e("dealWithTimeout", e.message.toString())
        }

        val result = withTimeoutOrNull(1300) { // 超时返回Null，正常结束返回最后一行
            repeat(1000) { rep ->
                Log.e("dealWithTimeout", "repeating: $rep ...")
                delay(500L)
            }
            "Done"
        } ?: "timeout"
        Log.e("dealWithTimeout", "task result is: $result")
    }
    // ----------------------- End ----------------------- //

    // ----------------------- CoroutineContext ----------------------- //
    // CoroutineContext的一组协程行为元素
    // Job:控制协程生命周期
    // CoroutineDispatcher:协程调度器，向合适的线程分发任务
    // CoroutineName
    // CoroutineExceptionHandler:处理未被捕捉的异常
    private fun context() {

    }
    // ----------------------- End ----------------------- //
}