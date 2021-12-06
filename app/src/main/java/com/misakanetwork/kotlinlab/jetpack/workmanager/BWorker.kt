package com.misakanetwork.kotlinlab.jetpack.workmanager

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

/**
 * Created By：Misaka10085
 * on：2021/12/2
 * package：com.misakanetwork.kotlinlab.jetpack.workmanager
 * class name：BWorker
 * desc：workManagerUse 任务链-A任务
 */
class BWorker(context: Context, workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {

    override fun doWork(): Result {
        Log.e("BWorker", "do BWork ${this.javaClass.simpleName}")
        return Result.success()
    }
}