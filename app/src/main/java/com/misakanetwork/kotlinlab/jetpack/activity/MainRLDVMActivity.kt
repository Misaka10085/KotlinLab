package com.misakanetwork.kotlinlab.jetpack.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.misakanetwork.kotlinlab.R
import com.misakanetwork.kotlinlab.jetpack.activity.viewmodel.MainRLDVMViewModel
import com.misakanetwork.kotlinlab.jetpack.adapter.MainRLDVMAdapter
import com.misakanetwork.kotlinlab.jetpack.dao.Teacher
import kotlinx.android.synthetic.main.activity_room_use.*

/**
 * Created By：Misaka10085
 * on：2021/11/30
 * package：com.misakanetwork.kotlinlab.jetpack.activity
 * class name：MainRLDVMActivity
 * desc：Room+LiveData+ViewModel
 */
class MainRLDVMActivity : AppCompatActivity(R.layout.activity_main_rldvm) {
    private lateinit var mainRLDVMViewModel: MainRLDVMViewModel
    private val mData: MutableList<Teacher> by lazy { mutableListOf() }

    companion object {
        fun startThis(context: Context) = context.startActivity(
            Intent(
                context.applicationContext,
                MainRLDVMActivity::class.java
            )
        )
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainRLDVMViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(application)
        )[MainRLDVMViewModel::class.java]
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mRecyclerView.adapter = MainRLDVMAdapter(this, mData)
        mainRLDVMViewModel.getAllTeachersLiveData().observe(this) {
            mData.clear()
            mData.addAll(it)
            mRecyclerView.adapter?.notifyDataSetChanged()
        }
    }

    fun mInsert(view: View) {
        mainRLDVMViewModel.insertTeacher(
            Teacher(name = "Quin ${(0..100).random()}", age = (0..100).random()),
            Teacher(name = "sfppp ${(0..100).random()}", age = (0..100).random())
        )
    }

    fun mDelete(view: View) {
        if (mData.isNullOrEmpty()) return
        val random = (0 until mData.size).random()
        val random2 = (0 until mData.size).random()
        mainRLDVMViewModel.deleteTeacher(
            Teacher(id = mData[random].id),
            Teacher(id = mData[random2].id)
        )
    }

    fun mUpdate(view: View) {
        if (mData.isNullOrEmpty()) return
        val random = (0 until mData.size).random()
        val random2 = (0 until mData.size).random()
        mData.forEachIndexed { index, item ->
            if (index == random || index == random2) {
                item.name = "Kenney ${(0..100).random()}"
                item.age = (0..99).random()
                mData[index] = item
            }
        }
        mainRLDVMViewModel.updateTeacher(mData[random], mData[random2])
    }

    fun mQuery(view: View) {
        mainRLDVMViewModel.getAllTeachersLiveData()
    }

    fun mClearAll(view: View) {
        mainRLDVMViewModel.deleteAllTeachers()
    }
}