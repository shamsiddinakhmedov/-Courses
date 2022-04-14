package com.example.pdponline.databse

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "module")
data class Module(

    @PrimaryKey(autoGenerate = true) val id: Int=0,
    var title: String,
    var placeNumber: Int,
    val coursesOwnerId: Int,
    val imageCourse: String
)
