package com.example.pdponline.databse

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.pdponline.databse.dao.CoursesDao
import com.example.pdponline.databse.dao.LessonDao
import com.example.pdponline.databse.dao.ModuleDao

@Database(entities = [Courses::class, Module::class, Lesson::class], version = 2)
abstract class AppDatabase: RoomDatabase() {
    abstract fun coursesDao(): CoursesDao
    abstract fun moduleDao(): ModuleDao
    abstract fun lessonDao(): LessonDao

    companion object{
        private var instance: AppDatabase? = null

        @Synchronized
        fun getInstance(context: Context): AppDatabase{
            if (instance == null){
                instance = Room.databaseBuilder(context, AppDatabase::class.java, "courses.db")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
            }
            return instance!!
        }
    }

}