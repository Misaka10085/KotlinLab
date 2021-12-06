package com.misakanetwork.kotlinlab.jetpack.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.misakanetwork.kotlinlab.R
import com.misakanetwork.kotlinlab.jetpack.adapter.RoomUseAdapter
import com.misakanetwork.kotlinlab.jetpack.dao.Student
import com.misakanetwork.kotlinlab.jetpack.dao.StudentDatabase
import kotlinx.android.synthetic.main.activity_room_use.*

/**
 * Created By：Misaka10085
 * on：2021/11/29
 * package：com.misakanetwork.kotlinlab.jetpack.activity
 * class name：RoomUseActivity
 * desc：RoomUseActivity
 *      @Entity修饰的Bean作为table
 *      @Dao增删改查接口
 *      @Database创建单例database引用
 *      异步调用或主线程调用
 */
class RoomUseActivity : AppCompatActivity() {
    private val mData: MutableList<Student> by lazy { mutableListOf() }
    private val database by lazy { StudentDatabase.instance }
    private val studentDao by lazy { database.getStudentDao() }

    companion object {
        fun startThis(context: Context) {
            context.startActivity(Intent(context.applicationContext, RoomUseActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room_use)
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mRecyclerView.adapter = RoomUseAdapter(this, mData)
        query_btn.callOnClick()
    }

    fun mInsert(view: View) {
//        studentDao.insertStudent(
//            Student(name = "Quin ${(0..100).random()}", age = (0..100).random()),
//            Student(name = "sfppp ${(0..100).random()}", age = (0..100).random())
//        )
//        query_btn.callOnClick()
        StudentInsertTask().execute(
            Student(
                name = "Quin ${(0..100).random()}",
                age = (0..100).random()
            ), Student(name = "sfppp ${(0..100).random()}", age = (0..100).random())
        )
    }

    @SuppressLint("StaticFieldLeak")
    inner class StudentInsertTask : AsyncTask<Student, Void, Void>() {
        override fun doInBackground(vararg students: Student): Void? {
            studentDao.insertStudent(*students)
            return null
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            query_btn.callOnClick()
        }
    }


    fun mDelete(view: View) {
        if (mData.isNullOrEmpty()) return
//        val random = (0 until mData.size).random()
//        mData.forEachIndexed { index, item ->
//            if (index == random) {
//                studentDao.deleteStudent(mData[index], mData[0])
//            }
//        }
//        query_btn.callOnClick()
        val random = (0 until mData.size).random()
        val random2 = (0 until mData.size).random()
        StudentDeleteTask().execute(Student(id = mData[random].id), Student(id = mData[random2].id))
    }

    @SuppressLint("StaticFieldLeak")
    inner class StudentDeleteTask : AsyncTask<Student, Void, Void>() {
        override fun doInBackground(vararg students: Student): Void? {
            studentDao.deleteStudent(*students)
            return null
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            query_btn.callOnClick()
        }
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
//                studentDao.updateStudent(mData[index])
            }
        }
        StudentUpdateTask().execute(mData[random], mData[random2])
//        query_btn.callOnClick()
    }

    @SuppressLint("StaticFieldLeak")
    inner class StudentUpdateTask : AsyncTask<Student, Void, Void>() {
        override fun doInBackground(vararg students: Student): Void? {
            studentDao.updateStudent(*students)
            return null
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            query_btn.callOnClick()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun mQuery(view: View) {
//        mData.clear()
//        mData.addAll(studentDao.getAllStudent())
//        mRecyclerView.adapter?.notifyDataSetChanged()
        StudentQueryTask().execute()
    }

    @SuppressLint("StaticFieldLeak")
    inner class StudentQueryTask : AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg students: Void): Void? {
            mData.clear()
            mData.addAll(studentDao.getAllStudent())
            return null
        }

        @SuppressLint("NotifyDataSetChanged")
        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            mRecyclerView.adapter?.notifyDataSetChanged()
        }
    }
}