package com.misakanetwork.kotlinlab.jetpack.dao

import androidx.room.*

/**
 * Created By：Misaka10085
 * on：2021/11/29
 * package：com.misakanetwork.kotlinlab.jetpack.dao
 * class name：StudentDao
 * desc：StudentDao
 */
@Dao
interface StudentDao {

    @Insert
    fun insertStudent(vararg student: Student)

    @Delete
    fun deleteStudent(vararg student: Student)

    @Update
    fun updateStudent(vararg student: Student)

    @Query("SELECT * FROM student")
    fun getAllStudent(): List<Student>

    @Query("SELECT * FROM student WHERE id=:id")
    fun getStudentById(id: Int): Student
}