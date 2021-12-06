package com.misakanetwork.kotlinlab.jetpack.adapter.common

import androidx.recyclerview.widget.RecyclerView
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

/**
 * Created By：Misaka10085
 * on：2021/4/13
 * package：com.misakanetwork.lib_common.adapter
 * class name：CommonViewHolder
 * desc：CommonViewHolder
 */
class CommonViewHolder(itemView: View?) : RecyclerView.ViewHolder(
    itemView!!
) {
    private val mView: SparseArray<View?> = SparseArray()
    fun <T : View?> getView(viewId: Int): T? {
        var view = mView[viewId]
        if (view == null) {
            view = itemView.findViewById(viewId)
            mView.put(viewId, view)
        }
        return view as T?
    }

    fun <T : ViewGroup?> getViewGroup(vieeId: Int): T? {
        var view = mView[vieeId]
        if (view == null) {
            view = itemView.findViewById(vieeId)
            mView.put(vieeId, view)
        }
        return view as T?
    }

    // 封装通用功能
    fun setText(viewId: Int, text: String?): CommonViewHolder {
        val textView = getView<TextView>(viewId)!!
        textView.text = text.toString()
        return this
    }

    fun setHintText(viewId: Int, text: CharSequence): CommonViewHolder {
        val view = getView<TextView>(viewId)!!
        view.hint = text.toString()
        return this
    }

    fun setImageResource(viewId: Int, resId: Int): CommonViewHolder {
        val view = getView<ImageView>(viewId)!!
        view.setImageResource(resId)
        return this
    }

}