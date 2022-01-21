package com.misakanetwork.kotlinlab.coroutine.activity

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.misakanetwork.kotlinlab.R
import com.misakanetwork.kotlinlab.coroutine.api.User
import com.misakanetwork.kotlinlab.coroutine.api.userServiceApi
import kotlinx.android.synthetic.main.activity_main_coroutine.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.selects.select
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.sync.withPermit
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.concurrent.atomic.AtomicInteger
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
//        createByOriginalApi()
//        dispatchers() // 调度器
//        coroutineScopes() // 协程作用域
        mainScope()
        viewModelScope() // 协程(ViewModelScope)+Retrofit+ViewModel+LiveData+DataBinding
//        coroutineLaunchAndAsync() // 网络请求等待、并发原理 runBlocking join await async组合并发
//        startMode() // 启动模式
//        scope() // 协程作用域与构建器
//        coroutineCancel()
//        use() // 自动释放资源
//        dealWithTimeout()
//        context() // 协程上下文、异常
//        flowCollectMultipleValue()
//        coldFlow()
//        hotChannel() // 热流Channel
//        concurrent() // 协程并发工具
        flowApp() // flow+协程+ViewBinding+LiveData/StateFlow+SharedFlow+Navigation+Retrofit+Room
        paging3()
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
                Log.e("coroutineLaunchAndAsync", "launch:Job1 finished")
            }
            val job2 = async { // 子协程，返回一个Job子类 Deferred,可以得到返回结果
                delay(200)
                Log.e("coroutineLaunchAndAsync", "async:Job2 finished")
                "job2 result"
            }
            // runBlocking会等待子协程全部执行完毕后关闭
            Log.e("coroutineLaunchAndAsync async", job2.await())

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
//        val job4 = async(context = Dispatchers.IO, start = CoroutineStart.DEFAULT) { // 子线程中调度然后执行
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
                    Log.e("coroutineCancel", "repeat sleeping...")
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
    private fun context() = runBlocking {
        launch(Dispatchers.Default + CoroutineName("contextTest")) {
            Log.e("CoroutineContext", "current working thread: ${Thread.currentThread().name}")
        }
        extends()
        catchError()
        exceptionByCancel()
        multipleErrorCatch() // 多异常聚合捕捉
    }

    @SuppressLint("LongLogTag")
    private fun extends() = runBlocking {
        // 协程上下文的继承，只有Job实例是不同的，若内部协程再次声明了CoroutineContext的元素，则覆盖对应修改
        val scope = CoroutineScope(Job() + Dispatchers.IO + CoroutineName("extendsTest"))
        val job = scope.launch {
            Log.e(
                "CoroutineContext",
                "outside context extends: \nJob:${coroutineContext[Job]} \nThread:${Thread.currentThread().name}"
            )
            val result = async { // 创建一个launch的子协程
                Log.e(
                    "CoroutineContext",
                    "inside context extends: \nJob:${coroutineContext[Job]} \nThread:${Thread.currentThread().name}"
                )
                "OK"
            }.await()
        }
        job.join()

        // 继承
        val coroutineExceptionHandle = CoroutineExceptionHandler { context, exception ->
            Log.e("CoroutineExceptionHandler", "Caught $exception")
        }
        val scope2 = CoroutineScope(Job() + Dispatchers.Main + coroutineExceptionHandle)
        val job2 = scope2.launch(Dispatchers.IO) {
            // 此处覆盖了父协程的Dispatchers，在IO线程运行
        }
    }

    @SuppressLint("LongLogTag")
    private fun catchError() = runBlocking {
        // 异常捕获处理
        // 子协程出现异常，会终止父协程下其他任务并向父级传递异常后终止自己
        val job3 = GlobalScope.launch {
            try {
                throw IndexOutOfBoundsException()
            } catch (e: Exception) {
                Log.e("errorCatch", "Caught IndexOutOfBoundsException")
            }
        }
        job3.join()
        val deferred = GlobalScope.async {
            throw ArithmeticException()
        }
        try {
            deferred.await() // 根协程的async方式启动，异常需要在被消费的时候捕获
        } catch (e: Exception) {
            Log.e("errorCatch", "Caught ArithmeticException")
        }

        // 非根协程的异常传播
        val scope4 = CoroutineScope(Job())
        val job4 = scope4.launch {
            async { // 非根协程的异常直接抛出
                try {
                    throw IllegalArgumentException()
                } catch (e: Exception) {
                    Log.e("非根协程的异常", "caught ${e.message}")
                }
            }
        }
        job4.join()

        // SupervisorJob():让子协程自己处理异常，不上抛、不影响其他子级
//        val supervisor = CoroutineScope(SupervisorJob())
//        val work1 = supervisor.launch {
//            delay(100)
//            Log.e("SupervisorJob", "child1")
//            throw IllegalArgumentException()
//        }
//        val work2 = supervisor.launch {
//            try {
//                delay(800)
//            } finally {
//                Log.e("SupervisorJob", "child2 finished")
//            }
//        }
//        delay(8200)
//        supervisor.cancel() // 用作用域取消所有协程
//        joinAll(work1, work2)

        // supervisorScope同样拥有以上特性，但在作用域自身出现异常，则子协程全部终止
//        supervisorScope {
//            val child = launch {
//                try {
//                    Log.e("supervisorScope error", "The child is sleeping")
//                    delay(Long.MAX_VALUE)
//                } finally {
//                    Log.e("supervisorScope error", "The child is cancelled")
//                }
//            }
//            yield()
//            Log.e("supervisorScope error", "Throwing an exception from scope")
//            throw AssertionError() // 所有子协程会被终止
//        }

        // async的异常在执行时才能被捕获，launch则自动抛出异常；
        // CoroutineExceptionHandler在根协程中或CoroutineScope的CoroutineContext（上下文）中才能捕获异常
        val coroutineExceptionHandle = CoroutineExceptionHandler { _, exception ->
            Log.e("CoroutineExceptionHandler", "Caught $exception")
        }
        val jobThrow = GlobalScope.launch(coroutineExceptionHandle) {
            throw AssertionError()
        }
//        val deferredThrow = GlobalScope.async(coroutineExceptionHandle) { // CoroutineExceptionHandler无法在此捕捉到异常
//            throw ArithmeticException()
//        }
        jobThrow.join()
//        deferredThrow.await() // async在根协程时的异常只能在此处捕捉

        val handler = CoroutineExceptionHandler { _, exception ->
            Log.e("CoroutineExceptionHandler", "Caught $exception")
        }
        val scope = CoroutineScope(Job())
        val job = scope.launch(handler) {
            launch {
                throw IllegalArgumentException() // 处于scope的上下文中，handler在根协程中可以捕获到
            }
        }
        job.join()

//        val job2 = scope.launch {
//            launch(handler) {
//                throw IllegalArgumentException() // 使用Job()，则异常会被上抛而handler在子协程中监视，则无法被捕获
//                // 也可不在根协程使用CoroutineExceptionHandler，在非根协程内部使用try catch
//            }
//        }
//        job2.join()

        // 协程全局异常处理器，该处理器不会捕获和处理异常、不会解决闪退：
        // 创建处理器GlobalCoroutineExceptionHandler继承CoroutineExceptionHandler
        // 创建File kotlinx.coroutines.CoroutineExceptionHandler，写入处理器路径
    }

    @SuppressLint("LongLogTag")
    private fun exceptionByCancel() = runBlocking {
        // cancel的异常会由协程自己处理，其他异常则会由父协程结束所有子协程后处理
        val job = launch {
            val child = launch {
                try {
                    delay(Long.MAX_VALUE)
                } finally {
                    Log.e("exceptionByCancel", "Child is cancelled")
                }
            }
            yield() // 放弃本次执行出让执行权
            Log.e("exceptionByCancel", "Cancelling child")
            child.cancelAndJoin() // cancel的异常会被协程自动处理
            yield()
            Log.e("exceptionByCancel", "Parent is now cancelled")
        }
        job.join()

        // 其它异常的捕获顺序
        val handler = CoroutineExceptionHandler { _, exception ->
            Log.e("CoroutineExceptionHandler", "Caught $exception")
        }
        val job2 = GlobalScope.launch(handler) {
            launch {
                try {
                    delay(Long.MAX_VALUE)
                } finally {
                    withContext(NonCancellable) {
                        Log.e(
                            "otherExceptions",
                            "Children are cancelled,but exception is not handled until all children terminate"
                        )
                        delay(100)
                        Log.e(
                            "otherExceptions",
                            "The first child finished its non cancellable block"
                        )
                    }
                }
            }
            launch {
                delay(10)
                Log.e("otherExceptions", "Second child throws another exception")
                throw ArithmeticException() // 该异常会在其它子协程结束完毕后才抛出
            }
        }
        job2.join()
    }

    @SuppressLint("LongLogTag")
    private fun multipleErrorCatch() = runBlocking {
        // 多异常聚合捕捉
        val handler = CoroutineExceptionHandler { _, exception ->
            Log.e(
                "MultipleExceptionHandler",
                "Caught $exception \n ${exception.suppressed.contentToString()}"
            )
        }
        val job = GlobalScope.launch(handler) {
            launch {
                try {
                    delay(Long.MAX_VALUE)
                } finally {
                    throw ArithmeticException()
                }
            }
            launch {
                try {
                    delay(Long.MAX_VALUE)
                } finally {
                    throw IndexOutOfBoundsException()
                }
            }
            launch {
                delay(500)
                throw IOException()
            }
        }
        job.join()
    }
    // ----------------------- End ----------------------- //

    // ----------------------- flowCollectMultipleValue ----------------------- //
    @SuppressLint("LongLogTag")
    private fun flowCollectMultipleValue() {
        // 集合返回多个值，但不是异步
        getMultipleResultByList().forEach { value ->
            Log.e(
                "flowCollectMultipleValue",
                "get multiple result by list:$value"
            )
        }
        // 序列返回多个值，但线程会阻塞运行该逻辑，且是同步
        getMultipleResultBySequence().forEach { value ->
            Log.e(
                "flowCollectMultipleValue",
                "get multiple result by sequence:$value"
            )
        }
        // 协程suspend集合异步获取多个值，但一次性返回多个
        getResultFromList()
        // Flow异步获取多个值，没有阻塞，且支持挂起
        getByFlow()
    }

    @SuppressLint("LongLogTag")
    private fun getByFlow() = runBlocking {
        launch {
            for (k in 1..3) {
                Log.e("flowCollectMultipleValue", "I'm not blocked $k")
                delay(1500)
            }
        }

        getResultByFlow().collect { value ->
            Log.e(
                "flowCollectMultipleValue",
                "get multiple result by flow:$value"
            )
        }
    }

    private fun getResultByFlow() = flow {
        for (i in 1..3) {
            delay(1000)
            emit(i) // 发送产生一个元素
        }
    }

    private fun getMultipleResultByList(): List<Int> = listOf(1, 2, 3)

    private fun getMultipleResultBySequence(): Sequence<Int> = sequence {
        for (i in 1..3) {
            Thread.sleep(1000) // 模拟计算
            yield(i)
        }
    }

    @SuppressLint("LongLogTag")
    private fun getResultFromList() = runBlocking {
        getMultipleResultFromList().forEach { value ->
            Log.e(
                "flowCollectMultipleValue",
                "get multiple result from list:$value"
            )
        }
    }

    private suspend fun getMultipleResultFromList(): List<Int> {
        delay(1000)
        return listOf(1, 2, 3)
    }
    // ----------------------- End ----------------------- //

    // ----------------------- coldFlow ----------------------- //
    private fun coldFlow() = runBlocking {
        // 冷流：只有调用collect的时候，构建器的代码才执行
        val flow = simpleFlow()
        Log.e("coldFlow", "Calling collect..")
        flow.collect { value -> Log.e("coldFlow", value.toString()) }
        Log.e("coldFlow", "Calling collect again..")
        flow.collect { value -> Log.e("coldFlow", value.toString()) }

        flowContinuation() // 流的连续性
        waysOfCreatingFlow()
        flowContext() // context flowOn切换线程
        cancelFlow()
        backPressure() // 背压
        flowOperator()
        exceptionFromFlow()
        flowComplete() // flow的完成
        multiplexingFlow() // flow使用多路复用
    }

    private fun simpleFlow() = flow {
        Log.e("coldFlow", "Flow started")
        for (i in 1..3) {
            delay(1000)
            emit(i)
        }
    }
    // ----------------------- End ----------------------- //

    // ----------------------- flowContinuation ----------------------- //
    private fun flowContinuation() = runBlocking {
        // 流的连续性:分别处理每个发射的结果，分别交给末端操作符
        (1..5).asFlow().filter {  // 构建一个1~5的流的过滤
            it % 2 == 0
        }.map { // 拼接字符串
            "string $it"
        }.collect {
            Log.e("flowContinuation", "Collect $it")
        }
    }
    // ----------------------- End ----------------------- //

    // ----------------------- waysOfCreatingFlow ----------------------- //
    private fun waysOfCreatingFlow() = runBlocking {
        // flow{}

        // flowOf
        flowOf("one", "two", "three")
            .onEach { delay(1000) } // onEach:每隔1s发射一个元素
            .collect {
                Log.e("flowOf", it)
            }

        // asFlow
        (1..3).asFlow().collect {
            Log.e("asFlow", it.toString())
        }
    }
    // ----------------------- End ----------------------- //

    // ----------------------- flowContext ----------------------- //
    private fun flowContext() = runBlocking {
        // 构建流不需要协程，collect收集流的时候需要协程；
        // collect会与构建流共享context，除非使用flowOn切换构建流所在的协程，或使用launchIn切换处理时所在协程
        simpleFlow2().collect { value ->
            Log.e("flowContext", "Collected $value ${Thread.currentThread().name}")
        }

        // flowOn切换构建流所在协程
        flowOnOtherwise().collect { value ->
            Log.e(
                "flowOn",
                "Collected $value ${Thread.currentThread().name}"
            )
        }

        // launchIn指定处理时所在协程
        val job = events().onEach { event ->
            Log.e(
                "launchIn",
                "onEach: $event ${Thread.currentThread().name}"
            )
        }
//            .collect { }
//            .launchIn(this) // 当前线程（主线程）下
            .launchIn(CoroutineScope(Dispatchers.IO))
//            .join()
        delay(200)
        job.cancelAndJoin()
    }

    private fun simpleFlow2() = flow {
        Log.e("flowContext", "Flow started ${Thread.currentThread().name}")
        emit(0)
    }

    private fun flowOnOtherwise() = flow {
        Log.e("flowOn", "FlowOn ${Thread.currentThread().name}")
        for (i in 9..12) {
            delay(1000)
            emit(i)
        }
    }.flowOn(Dispatchers.Default)

    /**
     * 事件源
     */
    private fun events() = (1..3)
        .asFlow()
        .onEach { delay(1000) }
        .flowOn(Dispatchers.Default)
    // ----------------------- End ----------------------- //

    // ----------------------- cancelFlow ----------------------- //
    private fun cancelFlow() = runBlocking {
        // 超时后取消流
        withTimeoutOrNull(5000) {
            simpleFlow4().collect { value -> Log.e("cancelFlow", value.toString()) }
        }
        Log.e("cancelFlow", "Done")

        // 流的取消检测机制 (每次emit都使用了ensureActive检查活性)
//        simpleFlow5().collect { value ->
//            Log.e("checkFlow", value.toString())
//            if (value == 3) cancel()
//        }
        // 频繁的任务取消太快需要明确cancellable()
//        (1..5).asFlow().cancellable().collect {
//            Log.e("checkFlow", it.toString())
//            if (it == 3) cancel()
//        }
    }

    private fun simpleFlow4() = flow {
        for (i in 44..48) {
            delay(1000)
            emit(i)
            Log.e("cancelFlow", "Emitting $i")
        }
    }

    private fun simpleFlow5() = flow {
        for (i in 1..5) {
            emit(i)
            Log.e("checkFlow", "Emitting $i")
        }
    }
    // ----------------------- End ----------------------- //

    // ----------------------- 背压 ----------------------- //
    // 背压:与水流同向的作用力，当生产者效率大于消费者效率的时候产生背压（如下载速度大于UI更新速度）
    private fun backPressure() = runBlocking {
        val time = measureTimeMillis {
            // 构建流生产速度大于collect消费速度，产生背压，不用collectLatest、conflate、buffer或切换线程时每个元素消耗300+100ms
            simpleFlow6()
//                .flowOn(Dispatchers.Default) // 自带缓冲，缓存所有emit后再执行下面collect
//                .buffer(50) // 缓冲，缓存emit，缩短执行时间（异步），消灭原有emit与collect并行执行的特性
//                .conflate() // 不对每个值都进行处理，会跳过部分中间的值
                .collectLatest { value -> // 只处理最后值
//                .collect { value ->
                    delay(300)
                    Log.e("backPressure", "Collected value $value ${Thread.currentThread().name}")
                }
        }
        Log.e("backPressure", "Collected in $time ms")
    }

    private fun simpleFlow6() = flow {
        for (i in 1..3) {
            delay(100)
            emit(i)
            Log.e("backPressure", "Emitting $i ${Thread.currentThread().name}")
        }
    }
    // ----------------------- End ----------------------- //

    // ----------------------- 流的操作符 ----------------------- //
    @SuppressLint("LongLogTag")
    private fun flowOperator() = runBlocking {
        // map
        (1..3).asFlow()
            .map { request -> performRequest(request) } // map遍历元素进行操作
            .collect { value -> Log.e("map", value) }

        // transform
        (1..3).asFlow()
            .transform { request -> // transform可对元素进行多次操作
                emit("Making request $request")
                emit(performRequest(request))
            }.collect { value -> Log.e("transform", value) }

        // take 只收集指定数量的元素(限长操作符)
        numbers().take(2).collect { value -> Log.e("take", value.toString()) }

        // terminal操作符
        val num = (1..5).asFlow()
            .map { it * it }
//            .first()
            .reduce { a, b -> a + b }
        Log.e("terminal", num.toString())

        // zip
        val numbers = (1..3).asFlow().onEach { delay(300) }
        val str = flowOf("Quin", "kenny", "sfppp").onEach { delay(400) }
        val startTime = System.currentTimeMillis()
        numbers.zip(str) { a, b -> "$a -> $b" } // 合并组合多个流
            .collect {
                Log.e("zip", "$it at ${System.currentTimeMillis() - startTime} ms from start")
            }

        // flatMapConcat（连接模式）、flatMapMerge、flatMapLatest
        val startFlatTime = System.currentTimeMillis()
        (1..3).asFlow()
            .onEach { delay(100) }
//            .map { requestFlow(it) } // 返回一个Flow<Flow<String>>
//            .flatMapConcat { requestFlow(it) } // 返回一个Flow<String>，即展开了内层的Flow
//            .flatMapMerge { requestFlow(it) } // 展平，取每一个emit元素与1~3结合
            .flatMapLatest { requestFlow(it) } // 展平，跳过中间部分值，取最新的
            .collect { value ->
                Log.e(
                    "flatMapConcat、flatMapMerge、flatMapLatest",
                    "$value at ${System.currentTimeMillis() - startFlatTime} ms from start"
                )
            }
    }

    /**
     * Int -> String
     */
    private suspend fun performRequest(request: Int): String {
        delay(1000)
        return "response $request"
    }

    private fun numbers() = flow {
        try {
            emit(1)
            emit(2)
            Log.e("take", "This line will not execute")
            emit(3)
        } finally {
            Log.e("take", "Finally in numbers")
        }
    }

    private fun requestFlow(i: Int) = flow {
        emit("$i：First")
        delay(500)
        emit("$i：Second")
    }
    // ----------------------- End ----------------------- //

    // ----------------------- 流的异常处理 ----------------------- //
    private fun exceptionFromFlow() = runBlocking {
        // collect时捕获异常
        try {
            simpleFlow7().collect { value ->
                Log.e("exceptionFromFlow", value.toString())
                check(value <= 1) { "Collected $value" }
            }
        } catch (e: Throwable) {
            Log.e("exceptionFromFlow", "Caught $e")
        }

        // 构建时捕获异常(不建议使用try catch捕获构建时的异常)
        flow {
            emit(1)
            throw ArithmeticException("Div 0")
        }.catch { e: Throwable ->
            Log.e("exceptionFromFlow", "Caught $e")
            emit(10) // catch后可以进行补充操作(可以用于在异常中进行恢复)
        }.flowOn(Dispatchers.IO).collect { Log.e("exceptionFromFlow", it.toString()) }
    }

    private fun simpleFlow7() = flow {
        for (i in 1..3) {
            Log.e("exceptionFromFlow", "Emitting $i")
            emit(i)
        }
    }
    // ----------------------- End ----------------------- //

    // ----------------------- 流的完成 ----------------------- //
    private fun flowComplete() = runBlocking {
        // 命令式finally（不使用Flow机制）
        try {
            simpleFlowN().collect { Log.e("flowComplete", "collected") }
        } finally {
            Log.e("flowComplete", "Done")
        }

        // onCompletion申明式处理
        simpleFlowN2()
            .onCompletion { exception -> // 不作捕获行为，只获取到异常信息
                if (exception != null) {
                    Log.e("flowComplete", "Done")
                }
            }
            .catch { exception -> Log.e("flowComplete", "caught $exception") } // 上游异常捕捉
            .collect { value ->
                Log.e("flowComplete", value.toString())
                check(value <= 1) { "Collected $value" } // 下游收集时的异常捕获
            }
    }

    private fun simpleFlowN() = (1..3).asFlow()

    private fun simpleFlowN2() = flow {
        emit(1)
        throw RuntimeException()
    }
    // ----------------------- End ----------------------- //

    // ----------------------- flow实现多路复用 ----------------------- //
    private fun multiplexingFlow() = runBlocking {
        // 将两个函数转换为协程后，再将协程转换为flow，最后flow合并
//        val name = "Quin"
//        coroutineScope {
//            listOf(::getUserFromLocal, ::getUserFromRemote)
//                .map { function ->
//                    function.call(name)
//                }.map { deferred ->
//                    flow { emit(deferred.await()) } // 反射两个函数转换为协程
//                }.merge() // 合并两个flow
//                .collect { user -> Log.e("multiplexingFlow", user.toString()) }
//        }
    }
    // ----------------------- End ----------------------- //

    // ----------------------- 热流Channel ----------------------- //
    private fun hotChannel() = runBlocking {
        // channel是一个并发安全的队列，用于协程间通信
        val channel = Channel<Int>()
        // 生产者
        val producer = GlobalScope.launch {
            var i = 0
            while (true) {
                delay(1000)
                channel.send(++i)
                Log.e("channel use", "send $i")
            }
        }
        // 消费者
        val consumer = GlobalScope.launch {
            while (true) {
                val element = channel.receive()
                Log.e("channel use", "receive $element")
            }
        }

        joinAll(producer, consumer)

        channelSize() // channel的容量（缓存区）
        iterateChannel() // channel的迭代
        produceAndActor() // 启动生产者协程与消费者协程
        closeChannel()
        broadcastChannel()
        multiplexingAwait() // await多路复用
        multiplexingChannel() // 多channel复用
        selectClause() // 能够被select的事件
    }
    // ----------------------- End ----------------------- //

    // ----------------------- channel流的容量 ----------------------- //
    private fun channelSize() = runBlocking {
        // 消费速度小于生产速度并且缓存区大小已满时(默认0)，channel挂起，待消费完后生产者才继续从缓冲队列里发送
        val channel = Channel<Int>()
        // 生产者生产后放入缓冲队列
        val producer = GlobalScope.launch {
            var i = 0
            while (true) {
                delay(1000)
                channel.send(++i)
                Log.e("channel use", "send $i")
            }
        }
        // 消费者
        val consumer = GlobalScope.launch {
            while (true) {
                delay(2000)
                val element = channel.receive()
                Log.e("channel use", "receive $element")
            }
        }

        joinAll(producer, consumer)
    }
    // ----------------------- End ----------------------- //

    // ----------------------- channel的迭代 ----------------------- //
    private fun iterateChannel() = runBlocking {
        val channel = Channel<Int>(Channel.UNLIMITED) // 设置缓冲区大小为MAX_VALUE
        // 生产者
        val producer = GlobalScope.launch {
            for (x in 1..5) {
                channel.send(x * x)
                Log.e("iterateChannel", "send ${x * x}")
            }
        }
        // 消费者 由于缓存区足够，待生产者生产并发送结束后依次取出收到的结果
        val consumer = GlobalScope.launch {
//            val iterator = channel.iterator()
//            while (iterator.hasNext()) {
//                val element = iterator.next()
//                Log.e("iterateChannel", "receive $element")
//                delay(2000)
//            }
            for (element in channel) { // 简洁写法
                Log.e("iterateChannel", "receive $element")
                delay(2000)
            }
        }

        joinAll(producer, consumer)
    }
    // ----------------------- End ----------------------- //

    // ----------------------- 启动生产者协程与消费者协程 ----------------------- //
    private fun produceAndActor() = runBlocking {
        // produce和actor的异常都在消费的时候捕捉
        // produce启动一个生产者协程，返回一个ReceiveChannel以供其它协程通过它来接收数据
        val produceChannel: ReceiveChannel<Int> = GlobalScope.produce { // 默认缓存区为0，机制同上面两个例子
            repeat(100) {
                delay(1000)
                send(it)
            }
        }
        val consumer = GlobalScope.launch { // 模拟一个消费者
            for (i in produceChannel) {
                Log.e("produceAndActor", "received $i")
            }
        }
        consumer.join()
        // actor启动一个消费者协程
        val actorChannel: SendChannel<Int> = GlobalScope.actor {
            while (true) {
                val element = receive()
                Log.e("produceAndActor", "actor get $element")
            }
        }
        val producer = GlobalScope.launch { // 模拟一个生产者
            for (i in 0..3) {
                actorChannel.send(i)
            }
        }
        producer.join()
    }
    // ----------------------- End ----------------------- //

    // ----------------------- 关闭channel ----------------------- //
    private fun closeChannel() = runBlocking {
        // channel调用close后，会等结果缓存中剩下的元素都被接收后关闭receive部分。建议由发起的主导者来执行close
        val channel = Channel<Int>(3)
        val producer = GlobalScope.launch {
            List(3) {
                channel.send(it)
                Log.e("closeChannel", "send $it")
            }
            channel.close()
            Log.e(
                "closeChannel", """close channel.
                        | - ClosedForSend:${channel.isClosedForSend}
                        | - ClosedForReceive:${channel.isClosedForReceive}""".trimMargin()
            )
        }
        val consumer = GlobalScope.launch {
            for (element in channel) {
                Log.e("closeChannel", "receive $element")
                delay(1000)
            }
            Log.e(
                "closeChannel", """After consuming.
                        | - ClosedForSend:${channel.isClosedForSend}
                        | - ClosedForReceive:${channel.isClosedForReceive}""".trimMargin()
            )
        }
        joinAll(producer, consumer)
    }
    // ----------------------- End ----------------------- //

    // ----------------------- broadcastChannel ----------------------- //
    private fun broadcastChannel() = runBlocking {
        // 生产者发送后可由多个接收者接收处理，即多个接收端不存在互斥行为
//        val channel = Channel<Int>()
//        val broadcastChannel = channel.broadcast(3)
        val broadcastChannel = BroadcastChannel<Int>(Channel.BUFFERED)
        val producer = GlobalScope.launch {
            List(3) {
                delay(100)
                broadcastChannel.send(it)
            }
            broadcastChannel.close()
        }
        List(3) { index ->
            GlobalScope.launch {
                val receiveChannel = broadcastChannel.openSubscription()
                for (i in receiveChannel) {
                    Log.e("broadcastChannel", "[#$index] received: $i")
                }
            }
        }.joinAll()
    }
    // ----------------------- End ----------------------- //

    // ----------------------- await多路复用 ----------------------- //
    private val cachePath = "coroutine.cache"
    private val gson = Gson()

    data class Response<T>(val value: T, val isLocal: Boolean)

    private fun multiplexingAwait() = runBlocking {
        // await多路复用的应用，由select自动选择最先返回结果的执行方法
        GlobalScope.launch {
            val localRequest = getUserFromLocal(5)
            val remoteRequest = getUserFromRemote(22)
            val userResponse = select<Response<User>> {
                // select自动选择最先有返回结果的，select泛型类型由返回值决定
                localRequest.onAwait { Response(it, true) }
                remoteRequest.onAwait { Response(it, false) }
            }
            userResponse.value?.let { Log.e("multiplexingAwait", it.toString()) }
        }.join()
    }

    fun CoroutineScope.getUserFromLocal(name: Int) =
        // 用扩展函数的方式代替GlobalScope.async的写法(async的await可以得到返回结果，select需要使用onAwait)
        async(Dispatchers.IO) {
//            delay(1000) // 模拟本地搜索耗时操作
            BufferedReader(InputStreamReader(assets.open(cachePath), "utf-8")).use { it ->
                val stringBuffer = StringBuffer()
                it.forEachLine {
                    stringBuffer.append(it)
                }
                stringBuffer.toString()
            }.let { gson.fromJson(it, User::class.java) }
        }

    fun CoroutineScope.getUserFromRemote(name: Int) = async(Dispatchers.IO) {
        userServiceApi.getUserLaws(name)
    }
    // ----------------------- End ----------------------- //

    // ----------------------- 多channel复用 ----------------------- //
    private fun multiplexingChannel() = runBlocking {
        val channels = listOf(Channel<Int>(), Channel<Int>())
        GlobalScope.launch {
            delay(100)
            channels[0].send(200)
        }
        GlobalScope.launch {
            delay(50)
            channels[1].send(100)
        }
        // 只接收发送最快的channel
        val result = select<Int?> {
            channels.forEach { channel ->
                channel.onReceive { it }
            }
        }
        Log.e("multiplexingChannel", result.toString())
    }
    // ----------------------- End ----------------------- //

    // ----------------------- selectClause ----------------------- //
    private fun selectClause() = runBlocking {
        // 可使用多路复用select的都为以下
        // SelectClause0:对应事件无返回值（onJoin启动的协程）
        // SelectClause1:对应事件有返回值（onAwait、onReceive）
        // SelectClause2:对应事件有返回值，且有一个额外的参数（Channel.onSend(sendMessage,resultCallback)）

        // SelectClause0
        val job1 = GlobalScope.launch {
            delay(100)
            Log.e("selectClause0", "job 1")
        }
        val job2 = GlobalScope.launch {
            delay(10)
            Log.e("selectClause0", "job 2")
        }
        select<Unit> {
            job1.onJoin { Log.e("selectClause0", "job 1 onJoin") }
            job2.onJoin { Log.e("selectClause0", "job 2 onJoin") }
        }
        delay(1000)

        // SelectClause2
        val channels = listOf(Channel<Int>(), Channel<Int>())
        Log.e("selectClause2", channels.toString())
        launch(Dispatchers.IO) {
            select<Int?> {
                launch {
                    delay(10)
                    channels[1].onSend(200) { sentChannel ->
                        Log.e("selectClause2", "sent on $sentChannel")
                    }
                }
                launch {
                    delay(100)
                    channels[0].onSend(100) { sentChannel ->
                        Log.e("selectClause2", "sent on $sentChannel")
                    }
                }
            }
        }
        GlobalScope.launch {
            Log.e("selectClause2", channels[0].receive().toString())
        }
        GlobalScope.launch {
            Log.e("selectClause2", channels[1].receive().toString())
        }
        delay(1000)
    }
    // ----------------------- End ----------------------- //

    // ----------------------- 协程并发工具 ----------------------- //
    private fun concurrent() {
        unsafeConcurrent()
        safeConcurrent()
        mutex() // 轻量级锁
        semaphore() // 轻量级信号量
        otherWays() // 不作协程并发安全
    }

    private fun unsafeConcurrent() = runBlocking {
        var count = 0
        List(1000) {
            GlobalScope.launch { count++ }
        }.joinAll()
        Log.e("unsafeConcurrent", count.toString())
    }

    private fun safeConcurrent() = runBlocking {
        var count = AtomicInteger(0)
        List(1000) {
            GlobalScope.launch { count.incrementAndGet() }
        }.joinAll()
        Log.e("safeConcurrent", count.get().toString())
    }

    private fun mutex() = runBlocking {
        var count = 0
        val mutex = Mutex()
        List(1000) {
            GlobalScope.launch {
                mutex.withLock { // 轻量级锁
                    count++
                }
            }
        }.joinAll()
        Log.e("mutex", count.toString())
    }

    private fun semaphore() = runBlocking {
        var count = 0
        val semaphore = Semaphore(1)
        List(1000) {
            GlobalScope.launch {
                semaphore.withPermit { // 轻量级信号量
                    count++
                }
            }
        }.joinAll()
        Log.e("semaphore", count.toString())
    }

    private fun otherWays() = runBlocking {
        var count = 0
        val result = count + List(1000) {
            GlobalScope.async { 1 }
        }.map { it.await() }.sum()
        Log.e("otherWays", result.toString())
    }
    // ----------------------- End ----------------------- //

    // ----------------------- flow+协程+ViewBinding+LiveData/StateFlow+SharedFlow+Navigation+Retrofit+Room ----------------------- //
    private fun flowApp() {
        flow_app_bt.setOnClickListener {
            FlowAppActivity.startThis(this)
        }
    }
    // ----------------------- End ----------------------- //

    // ----------------------- paging3 ----------------------- //
    private fun paging3() {
        paging3_bt.setOnClickListener {

        }
    }
    // ----------------------- End ----------------------- //
}