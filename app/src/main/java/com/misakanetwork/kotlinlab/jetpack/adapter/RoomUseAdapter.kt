package com.misakanetwork.kotlinlab.jetpack.adapter

import android.content.Context
import com.misakanetwork.kotlinlab.R
import com.misakanetwork.kotlinlab.jetpack.adapter.common.CommonAdapter
import com.misakanetwork.kotlinlab.jetpack.adapter.common.CommonViewHolder
import com.misakanetwork.kotlinlab.jetpack.dao.Student

/**
 * Created By：Misaka10085
 * on：2021/11/29
 * package：com.misakanetwork.kotlinlab.jetpack.adapter
 * class name：RoomUseAdapter
 * desc：RoomUseAdapter
 */
class RoomUseAdapter(context: Context, mData: MutableList<Student>) : CommonAdapter<Student>(
    context, mData,
    R.layout.item_room_use
) {

    override fun bindData(holder: CommonViewHolder?, position: Int) {
        val bean = mData?.get(position)
        holder?.setText(R.id.name_tv, bean?.name)
        holder?.setText(R.id.num_tv, bean?.id.toString())
        holder?.setText(R.id.age_tv, bean?.age.toString())
    }

    override fun getItemCount(): Int {
        return mData?.size!!
    }
}