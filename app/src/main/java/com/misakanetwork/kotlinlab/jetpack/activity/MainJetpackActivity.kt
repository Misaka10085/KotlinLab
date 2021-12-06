package com.misakanetwork.kotlinlab.jetpack.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.work.*
import com.misakanetwork.kotlinlab.ApplicationObserver
import com.misakanetwork.kotlinlab.R
import com.misakanetwork.kotlinlab.jetpack.activity.viewmodel.MainJetpackViewModel
import com.misakanetwork.kotlinlab.jetpack.service.JetpackLifecycleService
import com.misakanetwork.kotlinlab.jetpack.workmanager.*
import kotlinx.android.synthetic.main.activity_main_jetpack.*
import java.time.Duration
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created By：Misaka10085
 * on：2021/11/25
 * package：com.misakanetwork.kotlinlab.jetpack
 * class name：MainJetpackActivity
 * desc：Jetpack
 */
class MainJetpackActivity : AppCompatActivity() {
    private var elapsedTime: Long = 0
    private lateinit var viewModel: MainJetpackViewModel
    private var liveDataTestTimer: Timer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_jetpack)
        jetpackLifecycleObserverWay()
        lifecycleService()
        processLifecycleOwner() // 监听整个程序生命周期
        useOfViewModel()
        viewModelWithLiveData() // 用LiveData通知ViewModel更新布局
        useOfDataBinding()
        dataBindingWithViewModelAndLiveData()
        useOfRoom() // Room数据库
        roomWithLiveDataAndViewModel()
        navigation() // Navigation、PendingIntent
        workManagerUse()
        pagingUse()
    }

    // ----------------- 原生实现计时 ----------------- //
    override fun onResume() {
        super.onResume()
        chronometer_v.base =
            SystemClock.elapsedRealtime() - elapsedTime // 设置基准时间-开机时间，包括待机、睡眠时间。除去退到后台消耗的时间，只前台计时
        chronometer_v.start()
    }

    override fun onPause() {
        super.onPause()
        elapsedTime = SystemClock.elapsedRealtime() - chronometer_v.base // 计算退到后台运行的时间，用于削减
        chronometer_v.stop()
    }
    // ----------------- End ----------------- //

    // ----------------- jetpack实现计时 ----------------- //
    private fun jetpackLifecycleObserverWay() {
        // 解耦了系统组件与普通组件
        lifecycle.addObserver(lifecycle_ch_v) // 对Activity的生命周期进行绑定监听
    }
    // ----------------- End ----------------- //

    // ----------------- lifecycleService ----------------- //
    private fun lifecycleService() {
        lifecycle_service_start_btn.setOnClickListener {
            startService(Intent(this, JetpackLifecycleService::class.java))
        }
        lifecycle_service_stop_btn.setOnClickListener {
            stopService(Intent(this, JetpackLifecycleService::class.java))
        }
    }
    // ----------------- End ----------------- //

    // ----------------- processLifecycleOwner ----------------- //
    private fun processLifecycleOwner() {
        // 程序监听，针对整个程序的生命周期的监听，即Lifecycle.Event.ON_CREATE、Lifecycle.Event.ON_DESTROY只可能执行一次
        ProcessLifecycleOwner.get().lifecycle.addObserver(ApplicationObserver())
    }
    // ----------------- End ----------------- //

    // ----------------- ViewModel的使用 ----------------- //
    /**
     * 解决瞬态数据丢失
     * 避免异步调用内存泄漏
     * 缩小类维护
     */
    private fun useOfViewModel() {
        // 当Activity翻转，文本也会被重置（瞬态数据丢失）
//        view_model_test_btn.setOnClickListener {
//            view_model_test_tv.text = view_model_test_tv.text.let {
//                (it.toString().toInt() + 1).toString()
//            }
//        }
        // End
        // ViewModel使用，解决瞬态数据丢失
        // ViewModel独立于配置变化，即独立于Activity生命周期，无论Activity处于哪个生命周期ViewModel上的数据都可以访问
        // ViewModel不可传入Context，只能使用继承AndroidViewModel的ViewModel
        viewModel =
            ViewModelProvider(
                this,
                ViewModelProvider.AndroidViewModelFactory(application)
            )[MainJetpackViewModel::class.java] // 创建对应ViewModel，所有组件值从ViewModel中取
        view_model_test_tv.text = viewModel.number.toString()
        view_model_test_btn.setOnClickListener {
            view_model_test_tv.text = (++viewModel.number).toString()
        }
        //End
    }
    // ----------------- End ----------------- //

    // ----------------- LiveData+ViewModel的使用 ----------------- //
    /**
     * ViewModel中使用的LiveData:
     *      确保界面符合数据状态，数据始终保持最新状态，界面间可共享数据数据资源
     *      不会发生内存泄漏，不会因Activity停止而导致崩溃
     *      不需要关注生命周期
     */
    private fun viewModelWithLiveData() {
        // LiveData+ViewModel 实现通知控件更新
        // ViewModel中创建 MutableLiveData 类型的变量作为该TextView的值，并由ViewModel observe
        view_model_live_data_test_tv.text = viewModel.currentSecond?.value.toString()
        viewModel.currentSecond!!.observe((this), { // 监听该变量变化并处理
            view_model_live_data_test_tv.text = it.toString()
        })
        // Java:
//        viewModel.getCurrentSecond().observe(this, new Observer<Integer>() {
//            @Override
//            public void onChanged(Integer integer) {
//
//            }
//        });
        // End
        startTimer() // 定时器累加更新textView，viewModel observe中接收处理

        // LiveData+ViewModel 实现两个Fragment的联动:LiveDataUseFragment、LiveDataUseFragment2
        // 两个Fragment共同使用Activity的ViewModel observe即可实现同步更新对应组件
    }

    private fun startTimer() {
        liveDataTestTimer = liveDataTestTimer ?: let {
            liveDataTestTimer = Timer()
            liveDataTestTimer
        }
        // 定时器，延时1秒，间隔1秒累加
        liveDataTestTimer?.schedule(object : TimerTask() {
            override fun run() { // 异步线程(非UI线程)postValue()来累加改变LiveData
                viewModel.currentSecond?.postValue(viewModel.currentSecond?.value?.plus(1))
            }
        }, 1000, 1000)
    }
    // ----------------- End ----------------- //

    // ----------------- DataBinding的使用 ----------------- //
    private fun useOfDataBinding() {
        // build.gradle添加DataBinding为true，添加依赖，convert xml为DataBinding文件
        data_binding_use_btn.setOnClickListener {
            MainDataBindingActivity.startThis(this)
        }
    }
    // ----------------- End ----------------- //

    // ----------------- DataBinding+ViewModel+LiveData ----------------- //
    private fun dataBindingWithViewModelAndLiveData() {
        data_binding_view_model_live_data_btn.setOnClickListener {
            MainDBVMLDActivity.startThis(this)
        }
    }
    // ----------------- End ----------------- //

    // ----------------- Room数据库 ----------------- //
    private fun useOfRoom() {
        room_btn.setOnClickListener {
            RoomUseActivity.startThis(this)
        }
    }
    // ----------------- End ----------------- //

    // ----------------- Room+LiveData+ViewModel ----------------- //
    private fun roomWithLiveDataAndViewModel() {
        room_live_data_view_model_btn.setOnClickListener {
            MainRLDVMActivity.startThis(this)
        }
    }
    // ----------------- End ----------------- //

    // ----------------- Navigation、PendingIntent ----------------- //
    private fun navigation() {
        navigation_btn.setOnClickListener {
            NavigationUseActivity.startThis(this)
        }
    }
    // ----------------- End ----------------- //

    // ----------------- WorkManager ----------------- //
    private fun workManagerUse() {
        work_manager_add_btn.setOnClickListener {
            addWork()
        }
    }

    private fun addWork() {
        // 解决在后台不需要及时完成的任务，如发送应用程序日志、同步应用程序数据、备份用户数据等
        // WorkManager保证即使关机、关闭app后重新打开app依旧能够完成任务，但由于是循环队列执行所以不保证立即执行任务(由系统决定执行时间)
        // 由于厂商定制化系统严重，部分机型可能不会执行任务
        // 触发条件
        val constrains = Constraints.Builder()
            // NOT_REQUIRED:对网络没有要求
            // CONNECTED:网络连接的时候执行，网络恢复的时候不会立即执行
            // UNMETERED:不计费的网络下(WIFI)执行
            // NOT_ROAMING:非漫游网络状态下执行
            // METERED:计费网络下执行
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
//            .setRequiresBatteryNotLow(true) // 在电量不足条件下执行
//            .setRequiresCharging(true) // 在充电时执行
//            .setRequiresStorageNotLow(true) // 不在存储容量不足时执行
//            .setRequiresDeviceIdle(true) // 在待机状态下执行 API>=23
            .build()
        // 设置参数
        val inputData = Data.Builder()
            .putString("input_data", "Quin")
            .build()
        // 配置任务
        val request = OneTimeWorkRequest.Builder(WorkManagerUseWorker::class.java)
            .setConstraints(constrains) // 设置触发条件
            .setInitialDelay(5, TimeUnit.SECONDS) // 设置延时执行
            .setBackoffCriteria(
                BackoffPolicy.LINEAR, // 线性增长间隔
                Duration.ofSeconds(2)
            ) // 指数退避策略，当不符合执行条件时的重复执行策略 API>=26
            .addTag("workRequest1") // 设置标签索引
            .setInputData(inputData) // 参数传递
            .build()
        // 提交任务给WorkManager
        val workManager = WorkManager.getInstance(this)
        workManager.enqueue(request) // 添加到队列
        // 监听任务状态
        workManager.getWorkInfoByIdLiveData(request.id).observe(this) {
            it?.let {
                Log.e("workManager", it.toString())
                if (it.state == WorkInfo.State.SUCCEEDED) {
                    val outputData = it.outputData.getString("output_data")
                    Log.e("workManager complete", "outputData: ${outputData.toString()}")
                }
            }
        }
        // 取消任务
//        Timer().schedule(
//            object : TimerTask() {
//                override fun run() {
//                    workManager.cancelWorkById(request.id)
//                }
//            },
//            2000
//        )

        // 创建周期性任务
//        val workRequest =
//            PeriodicWorkRequest.Builder(
//                WorkManagerUseWork::class.java,
//                Duration.ofMinutes(15)
//            ) // 最少15min间隔周期执行

        val workA = OneTimeWorkRequest.Builder(AWorker::class.java).build()
        val workB = OneTimeWorkRequest.Builder(BWorker::class.java).build()
        val workC = OneTimeWorkRequest.Builder(CWorker::class.java).build()
        val workD = OneTimeWorkRequest.Builder(DWorker::class.java).build()
        val workE = OneTimeWorkRequest.Builder(EWorker::class.java).build()
        // 任务链，依次执行任务
        WorkManager.getInstance(this)
            .beginWith(workA)
            .then(workB)
            .enqueue()
        // 任务组合，先执行完combine的组合，再执行then
        val workContinuationAB = WorkManager.getInstance(this)
            .beginWith(workA)
            .then(workB)
        val workContinuationCD = WorkManager.getInstance(this)
            .beginWith(workC)
            .then(workD)
        val workContinuationTaskList = listOf(workContinuationAB, workContinuationCD)
        WorkContinuation.combine(workContinuationTaskList)
            .then(workE)
            .enqueue()
    }
    // ----------------- End ----------------- //

    // ----------------- WorkManager ----------------- //
    private fun pagingUse() {
        paging_use_btn.setOnClickListener {
            MainPagingActivity.startThis(this)
        }
    }
    // ----------------- End ----------------- //
}