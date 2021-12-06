package com.misakanetwork.kotlinlab.jetpack.workmanager

import android.content.Context
import android.util.Log
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters

/**
 * Created By：Misaka10085
 * on：2021/12/2
 * package：com.misakanetwork.kotlinlab.jetpack.workmanager
 * class name：WorkManagerUseWork
 * desc：WorkManagerUseWork 需要添加依赖
 */
class WorkManagerUseWorker(context: Context, workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {

    override fun doWork(): Result {
        val dataString = inputData.getString("input_data")
        Log.e("WorkManagerUseWork", "data $dataString")
//        SystemClock.sleep(2000)
        Log.e("WorkManagerUseWork", "manager do work")
//        return Result.success() // 执行成功
//        return Result.success() // 未成功，重试
        // 执行结束后返回数据
        return Result.success(
            Data.Builder()
                .putString("output_data", "success data")
                .build()
        )
    }
}