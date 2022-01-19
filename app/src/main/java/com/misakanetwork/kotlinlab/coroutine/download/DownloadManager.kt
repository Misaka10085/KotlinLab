package com.misakanetwork.kotlinlab.coroutine.download

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

/**
 * Created By：Misaka10085
 * on：2022/1/19
 * package：com.misakanetwork.kotlinlab.coroutine.download
 * class name：DownloadManager
 * desc：DownloadManager
 */
object DownloadManager {
    fun download(url: String, file: File): Flow<DownloadStatus> {

        return flow {
            val request = Request.Builder().url(url).get().build()
            val response = OkHttpClient.Builder().build().newCall(request).execute()
            if (response.isSuccessful) {
                response.body()!!.let { body -> // 空情况交由flow的catch捕获
                    val total = body.contentLength()
                    // 文件读写
                    file.outputStream().use { output ->
                        val input = body.byteStream()
                        var emittedProgress = 0L
                        input.copyTo(output) { bytesCopied ->
                            val progress = bytesCopied * 100 / total
                            if (progress - emittedProgress > 5) {
                                delay(100)
                                emit(DownloadStatus.Progress(progress.toInt()))
                                emittedProgress = progress
                            }
                        }
                    }
                }
                emit(DownloadStatus.Done(file))
            } else {
                throw IOException(response.toString())
            }
        }.catch {
            file.delete()
            emit(DownloadStatus.Error(it))
        }.flowOn(Dispatchers.IO)
    }

    /**
     * inputSteam-copy->outputStream
     */
    private inline fun InputStream.copyTo(
        out: OutputStream,
        bufferSize: Int = DEFAULT_BUFFER_SIZE,
        progress: (Long) -> Unit
    ): Long {
        var bytesCopied: Long = 0
        val buffer = ByteArray(bufferSize)
        var bytes = read(buffer)
        while (bytes >= 0) {
            out.write(buffer, 0, bytes)
            bytesCopied += bytes
            bytes = read(buffer)

            progress(bytesCopied)
        }
        return bytesCopied
    }
}


