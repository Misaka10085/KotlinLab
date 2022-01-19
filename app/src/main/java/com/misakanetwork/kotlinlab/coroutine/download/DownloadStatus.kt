package com.misakanetwork.kotlinlab.coroutine.download

import java.io.File

/**
 * Created By：Misaka10085
 * on：2022/1/19
 * package：com.misakanetwork.kotlinlab.coroutine.download
 * class name：DownloadStatus
 * desc：Sealed 下载状态
 */
sealed class DownloadStatus {
    object None : DownloadStatus()
    data class Progress(val value: Int) : DownloadStatus()
    data class Error(val throwable: Throwable) : DownloadStatus()
    data class Done(val file: File) : DownloadStatus()
}
