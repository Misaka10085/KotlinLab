package com.misakanetwork.kotlinlab.jetpack.adapter.common

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * Created By：Misaka10085
 * on：2021/4/13
 * package：com.misakanetwork.lib_common.adapter
 * class name：CommonAdapter
 * desc：CommonAdapter
 */
abstract class CommonAdapter<T> : RecyclerView.Adapter<CommonViewHolder> {
    protected var TAG = this.javaClass.simpleName
    private var mInflater: LayoutInflater
    var mData: MutableList<T>? = null
    var mDataBean: T? = null
    var mContext: Context
    private var mLayoutId = -1
    private var mTypeSupport: MultipleType<T>? = null

    constructor(mContext: Context, mData: MutableList<T>?) {
        this.mData = mData
        mLayoutId = -1
        this.mContext = mContext
        mInflater = LayoutInflater.from(mContext)
    }

    constructor(mContext: Context, mDataBean: T) {
        this.mDataBean = mDataBean
        mLayoutId = -1
        this.mContext = mContext
        mInflater = LayoutInflater.from(mContext)
    }

    constructor(mContext: Context, mData: MutableList<T>?, mLayoutId: Int) {
        this.mData = mData
        this.mLayoutId = mLayoutId
        this.mContext = mContext
        mInflater = LayoutInflater.from(mContext)
    }

    constructor(mContext: Context, mData: MutableList<T>?, mTypeSupport: MultipleType<T>?) {
        this.mData = mData
        this.mContext = mContext
        this.mTypeSupport = mTypeSupport
        mInflater = LayoutInflater.from(mContext)
    }

    fun clearData() {
        mData!!.clear()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommonViewHolder {
        if (mTypeSupport != null) {
            mLayoutId = viewType
        }
        val view = mInflater.inflate(mLayoutId, parent, false)
        return CommonViewHolder(view)
    }

    override fun getItemViewType(position: Int): Int {
        return if (mTypeSupport != null) {
            mTypeSupport!!.getLayoutId(mData!![position], position)
        } else {
            super.getItemViewType(position)
        }
    }

    override fun onBindViewHolder(holder: CommonViewHolder, position: Int) {
        bindData(holder, position)
    }

    override fun getItemCount(): Int {
        return if (mData == null) 0 else mData!!.size
    }

    protected abstract fun bindData(holder: CommonViewHolder?, position: Int)
}