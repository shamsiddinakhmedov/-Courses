package com.example.pdponline.databse.dao

import androidx.room.*
import com.example.pdponline.databse.CourseWithModule
import com.example.pdponline.databse.Courses
import io.reactivex.Flowable

@Dao
interface CoursesDao {
    @Transaction
    @Query("select * from course")
    fun getAllCourses(): Flowable<List<CourseWithModule>>

    @Insert
    fun insertCourse(courses: Courses)

    @Delete
    fun deleteCourse(courses: Courses)

    @Update
    fun updateCourse(courses: Courses)

    @Query("select * from course")
    fun getAllC(): List<Courses>

    @Query("select * from course where id = :id")
    fun getCourseById(id: Int): CourseWithModule


}