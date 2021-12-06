package com.misakanetwork.kotlinlab.jetpack.workmanager

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

/**
 * Created By：Misaka10085
 * on：2021/12/2
 * package：com.misakanetwork.kotlinlab.jetpack.workmanager
 * class name：EWorker
 * desc：workManagerUse 任务链-A任务
 */
class EWorker(context: Context, workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {

    override fun doWork(): Result {
        Log.e("EWorker", "do EWork ${this.javaClass.simpleName}")
        return Result.success()
    }
}