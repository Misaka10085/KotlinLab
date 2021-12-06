package com.misakanetwork.kotlinlab.jetpack.dao

import android.annotation.SuppressLint
import android.os.AsyncTask
import androidx.lifecycle.LiveData

/**
 * Created By：Misaka10085
 * on：2021/11/30
 * package：com.misakanetwork.kotlinlab.jetpack.dao
 * class name：TeacherRepository
 * desc：Room+LiveData+ViewModel 's Repository
 * ViewModel通过它与Room交互
 */
class TeacherRepository {
    private val teacherDao: TeacherDao = TeacherDatabase.instance.getTeacherDao()

    fun insertTeacher(vararg teachers: Teacher) {
        TeacherInsertTask().execute(*teachers)
    }

    @SuppressLint("StaticFieldLeak")
    inner class TeacherInsertTask : AsyncTask<Teacher, Void, Void>() {
        override fun doInBackground(vararg teachers: Teacher): Void? {
            teacherDao.insertTeacher(*teachers)
            return null
        }
    }

    fun deleteTeacher(vararg teachers: Teacher) {
        TeacherDeleteTask().execute(*teachers)
    }

    @SuppressLint("StaticFieldLeak")
    inner class TeacherDeleteTask : AsyncTask<Teacher, Void, Void>() {

        override fun doInBackground(vararg teachers: Teacher): Void? {
            teacherDao.deleteTeacher(*teachers)
            return null
        }
    }

    fun deleteAllTeachers() {
        AllTeacherDeleteTask().execute()
    }

    @SuppressLint("StaticFieldLeak")
    inner class AllTeacherDeleteTask : AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg voids: Void): Void? {
            teacherDao.deleteAllTeachers()
            return null
        }
    }

    fun updateTeacher(vararg teachers: Teacher) {
        TeacherUpdateTask().execute(*teachers)
    }

    @SuppressLint("StaticFieldLeak")
    inner class TeacherUpdateTask : AsyncTask<Teacher, Void, Void>() {
        override fun doInBackground(vararg teachers: Teacher): Void? {
            teacherDao.updateTeacher(*teachers)
            return null
        }
    }

    fun getAllTeachersLiveData(): LiveData<List<Teacher>> = teacherDao.getAllTeachersLiveData()
}