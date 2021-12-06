package com.misakanetwork.kotlinlab.jetpack.adapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.misakanetwork.kotlinlab.jetpack.utils.LoadingImageUtils

/**
 * Created By：Misaka10085
 * on：2021/11/26
 * package：com.misakanetwork.kotlinlab.jetpack.adapter
 * class name：DataBindingImageLoadAdapter
 * desc：处理DataBinding网络图片加载
 */
object DataBindingImageLoadAdapter {

    @JvmStatic
    @BindingAdapter(value = ["image", "small"], requireAll = false)
    fun setImage(imageView: ImageView, url: String, small: Boolean = false) =
        LoadingImageUtils.loadImage(url, imageView, small)

    @JvmStatic
    @BindingAdapter(value = ["image", "small", "default"], requireAll = false)
    fun setImageElseSetResource(
        imageView: ImageView,
        url: String?,
        small: Boolean = false,
        default: Int
    ) = if (!url.isNullOrEmpty()) LoadingImageUtils.loadImage(
        url,
        imageView,
        small
    ) else imageView.setImageResource(default)
}