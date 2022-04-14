package com.example.pdponline.databse

import androidx.room.Embedded
import androidx.room.Relation

data class ModuleWithLesson(
    @Embedded val module: Module,
    @Relation(parentColumn = "id",entityColumn = "moduleOwnerId")
    val lesson: List<Lesson>
)

