package com.example.pdponline.databse.dao

import androidx.room.*
import com.example.pdponline.databse.Lesson
import io.reactivex.Flowable

@Dao
interface LessonDao {
    @Query("select * from lesson where moduleOwnerId =:id order by placeNumber")
    fun getAllLessonsById(id: Int): List<Lesson>

    @Insert
    fun insertLesson(lesson: Lesson)

    @Delete
    fun deleteLesson(lesson: Lesson)

    @Update
    fun updateLesson(lesson: Lesson)

    @Query("select * from lesson where moduleOwnerId = :id order by placeNumber")
    fun getAllLessons(id: Int): Flowable<List<Lesson>>

    @Query("select * from lesson where moduleOwnerId = :id")
    fun getAllLessonsByModuleId(id: Int): List<Lesson>

    @Query("select * from lesson where id = :id")
    fun getLessonsById(id: Int): Lesson
}