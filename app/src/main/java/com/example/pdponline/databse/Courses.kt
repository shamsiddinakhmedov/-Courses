package com.example.pdponline.databse

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "course")
data class Courses(
    @PrimaryKey(autoGenerate = true) var id: Int=0,
    var title: String,
    var courseIcon: String

)