package com.misakanetwork.kotlinlab.jetpack.utils

import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions

/**
 * Created By：Misaka10085
 * on：2021/11/26
 * package：com.misakanetwork.kotlinlab.jetpack.utils
 * class name：LoadingImageUtils
 * desc：LoadingImageUtils
 */
object LoadingImageUtils {
    private val TAG = LoadingImageUtils::class.java.simpleName
    private var defaultPlaceHolder: Int? = null
    private var defaultErrorHolder: Int? = null
    private var defaultHeadIconHolder: Int? = null
    private var defaultDecodeFormat: DecodeFormat = DecodeFormat.PREFER_ARGB_8888

    fun loadImage(url: String, imageView: ImageView?, small: Boolean) {
        imageView?.let {
            Glide.with(imageView.context.applicationContext)
                .load(url)
                .apply(
                    getDefaultRequestOptions(
                        defaultPlaceHolder, defaultErrorHolder,
                        if (small) DecodeFormat.PREFER_RGB_565 else DecodeFormat.PREFER_ARGB_8888
                    )
                )
                .into(imageView)
        } ?: let {
            Log.e(TAG, ">>> loadAutoScaleImage imageView==null")
        }
    }

    private fun getDefaultRequestOptions(
        customPlaceHolder: Int?,
        customErrorHolder: Int?,
        customDecodeFormat: DecodeFormat
    ): RequestOptions = if (customPlaceHolder != null && customErrorHolder != null) {
        RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(customPlaceHolder)
            .error(customErrorHolder)
            .format(customDecodeFormat)
    } else if (customPlaceHolder != null) {
        RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(customPlaceHolder)
            .format(customDecodeFormat)
    } else if (customErrorHolder != null) {
        RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .error(customErrorHolder)
            .format(customDecodeFormat)
    } else {
        RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .format(customDecodeFormat)
    }
}