package com.misakanetwork.kotlinlab.jetpack.dao

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * Created By：Misaka10085
 * on：2021/11/29
 * package：com.misakanetwork.kotlinlab.jetpack.dao
 * class name：TeacherDao
 * desc：TeacherDao
 */
@Dao
interface TeacherDao {

    @Insert
    fun insertTeacher(vararg teacher: Teacher)

    @Delete
    fun deleteTeacher(vararg teacher: Teacher)

    @Query("DELETE FROM `teacher.db`")
    fun deleteAllTeachers()

    @Update
    fun updateTeacher(vararg teacher: Teacher)

    @Query("SELECT * FROM `teacher.db`")
    fun getAllTeachersLiveData(): LiveData<List<Teacher>>

    @Query("SELECT * FROM `teacher.db` WHERE id=:id")
    fun getTeacherById(id: Int): Teacher
}