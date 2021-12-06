package com.misakanetwork.kotlinlab.jetpack.dao

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.misakanetwork.kotlinlab.MyApplication

/**
 * Created By：Misaka10085
 * on：2021/11/29
 * package：com.misakanetwork.kotlinlab.jetpack.dao
 * class name：TeacherDatabase
 * desc：database
 */
@Database(
    entities = [Teacher::class],
    version = 4,
    exportSchema = true
) // 使用导出schema记录文件需要在build.gradle中配置路径
abstract class TeacherDatabase : RoomDatabase() {

    abstract fun getTeacherDao(): TeacherDao

    companion object {
        val instance = Single.sin
    }

    private object Single {
        // Migration数据库升级，table中需要同步添加对应字段
        val MIGRATION_1_2: Migration by lazy {
            object : Migration(1, 2) {
                override fun migrate(database: SupportSQLiteDatabase) {
                    database.execSQL("ALTER TABLE 'teacher.db' ADD COLUMN sex INTEGER NOT NULL DEFAULT 1")
                }
            }
        }
        val MIGRATION_2_3: Migration by lazy {
            object : Migration(2, 3) {
                override fun migrate(database: SupportSQLiteDatabase) {
                    database.execSQL("ALTER TABLE 'teacher.db' ADD COLUMN bar_data INTEGER NOT NULL DEFAULT 1")
                }
            }
        }
        val MIGRATION_3_4: Migration by lazy { // 字段类型修改版本，需要根据json文件判断是否需要NOT NULL,tableEntity也需要同步修改
            object : Migration(3, 4) {
                override fun migrate(database: SupportSQLiteDatabase) {
                    database.execSQL( // integer not null改为text
                        "CREATE TABLE 'temp_teacher.db' (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, name TEXT NOT NULL, age INTEGER NOT NULL, sex TEXT DEFAULT 'M', bar_data INTEGER NOT NULL)"
                    )
                    database.execSQL(
                        "INSERT INTO 'temp_teacher.db' (name,age,sex,bar_data) SELECT name,age,sex,bar_data FROM 'teacher.db'"
                    )
                    database.execSQL("DROP TABLE 'teacher.db'")
                    database.execSQL("ALTER TABLE 'temp_teacher.db' RENAME TO 'teacher.db'")
                }
            }
        }

        val sin: TeacherDatabase = Room.databaseBuilder(
            MyApplication.instance(),
            TeacherDatabase::class.java,
            "teacher.db"
        )
//            .allowMainThreadQueries() // 允许在主线程进行数据操作
//            .addMigrations(MIGRATION_1_2)
//            .addMigrations(MIGRATION_1_2, MIGRATION_2_3) // 添加升级版本，version需要对应增加，跳版本的安装会按顺序执行
            .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4)
//            .fallbackToDestructiveMigration() // 数据异常时、字段类型修改时使用，退回到原始版本，但是数据会被清空
//            .createFromAsset("preTeacher.db") // 预填充数据库，在main下assets文件夹中放入对应数据库即可
            .build()
    }
}