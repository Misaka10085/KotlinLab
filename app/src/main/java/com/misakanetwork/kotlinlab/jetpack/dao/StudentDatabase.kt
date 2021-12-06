package com.misakanetwork.kotlinlab.jetpack.dao

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.misakanetwork.kotlinlab.MyApplication

/**
 * Created By：Misaka10085
 * on：2021/11/29
 * package：com.misakanetwork.kotlinlab.jetpack.dao
 * class name：StudentDatabase
 * desc：database
 */
@Database(entities = [Student::class], version = 1, exportSchema = false)
abstract class StudentDatabase : RoomDatabase() {

    abstract fun getStudentDao(): StudentDao

    companion object {
        val instance = Single.sin
    }

    private object Single {
        val sin: StudentDatabase = Room.databaseBuilder(
            MyApplication.instance(),
            StudentDatabase::class.java,
            "student"
        )
//            .allowMainThreadQueries() // 允许在主线程进行数据操作
            .build()
    }

    // java:
//    private static synchronized StudentDatabase(Context context){
//        if(mInstance==null){
//            mInstance=Room.databaseBuilder(
//                context.getApplicationContext(),
//                StudentDatabase.class,
//                        "student"
//            )
//                .build
//        }
//    }
}