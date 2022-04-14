package com.example.pdponline

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.pdponline.databinding.ActivityLessonInfoBinding
import com.example.pdponline.databse.AppDatabase

class LessonInfoActivity : AppCompatActivity() {
    lateinit var binding: ActivityLessonInfoBinding
    lateinit var appDatabase: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLessonInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        appDatabase = AppDatabase.getInstance(this)

        val id = intent.getIntExtra("lessonId", 1)
        val lesson = appDatabase.lessonDao().getLessonsById(id)

        binding.back.setOnClickListener {
           finish()
        }

        binding.lessonNumber.text = "${lesson.placeNumber}-dars"
        binding.lessonName.text = lesson.title
        binding.lessonDescription.text = lesson.description
    }
}