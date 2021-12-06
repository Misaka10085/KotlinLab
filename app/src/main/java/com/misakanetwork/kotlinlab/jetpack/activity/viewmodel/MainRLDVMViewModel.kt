package com.misakanetwork.kotlinlab.jetpack.activity.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.misakanetwork.kotlinlab.jetpack.dao.Teacher
import com.misakanetwork.kotlinlab.jetpack.dao.TeacherRepository

/**
 * Created By：Misaka10085
 * on：2021/11/30
 * package：com.misakanetwork.kotlinlab.jetpack.activity.viewmodel
 * class name：MainRLDVMViewModel
 * desc：Room+LiveData+ViewModel 's ViewModel
 */
class MainRLDVMViewModel : ViewModel() {
    private val teacherRepository: TeacherRepository = TeacherRepository()

    fun insertTeacher(vararg teachers: Teacher) {
        teacherRepository.insertTeacher(*teachers)
    }

    fun deleteTeacher(vararg teachers: Teacher) {
        teacherRepository.deleteTeacher(*teachers)
    }

    fun deleteAllTeachers() {
        teacherRepository.deleteAllTeachers()
    }

    fun updateTeacher(vararg teacher: Teacher) {
        teacherRepository.updateTeacher(*teacher)
    }

    fun getAllTeachersLiveData(): LiveData<List<Teacher>> =
        teacherRepository.getAllTeachersLiveData()
}