package com.example.pdponline.databse

import androidx.room.Embedded
import androidx.room.Relation

data class CourseWithModule(
    @Embedded val course: Courses,
    @Relation(
        parentColumn = "id",
        entityColumn = "coursesOwnerId"
    )
    var modules: List<Module>
)