package com.example.pdponline

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.pdponline.databinding.ActivityEditLessonBinding
import com.example.pdponline.databse.AppDatabase
import com.google.android.material.snackbar.Snackbar

class EditLessonActivity : AppCompatActivity() {
    lateinit var binding: ActivityEditLessonBinding
    lateinit var appDatabase: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditLessonBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val id = intent.getIntExtra("lessonId", 1)
        appDatabase = AppDatabase.getInstance(this)
        val lesson = appDatabase.lessonDao().getLessonsById(id)

        binding.lName.setText(lesson.title)
        binding.lDescription.setText(lesson.description)
        binding.lNumber.setText(lesson.placeNumber.toString())

        binding.checkIcon.setOnClickListener {
            val etLessonName = findViewById<View>(R.id.lName) as EditText
            val strLessonName = etLessonName.text.toString()

            val etLessonDescription = findViewById<View>(R.id.lDescription) as EditText
            val strLessonDescription = etLessonDescription.text.toString()

            val etLessonNumber = findViewById<View>(R.id.lNumber) as EditText
            val strLessonNumber = etLessonNumber.text.toString()

            when {
                TextUtils.isEmpty(strLessonName.trim()) -> {
                    etLessonName.error = "Bo'sh maydonni to'ldiring"
                }
                TextUtils.isEmpty(strLessonDescription.trim()) -> {
                    etLessonDescription.error = "Bo'sh maydonni to'ldiring"
                }
                TextUtils.isEmpty(strLessonNumber.trim()) -> {
                    etLessonNumber.error = "Bo'sh maydonni to'ldiring"
                }
                else -> {
                    lesson.title = strLessonName
                    lesson.description = strLessonDescription
                    lesson.placeNumber = strLessonNumber.toInt()

//                    var isHave = false
//                    for (i in lessonList.indices) {
//                        if (strLessonName.trim() == lessonList[i].title.trim() || strLessonNumber == lessonList[i].placeNumber.toString()) {
//                            isHave = true
//                            break
//                        }
//                    }
//                    if (isHave) {
//                        val snackBar = Snackbar.make(
//                            it,
//                            "Bunday kurs ro'yxatda mavjud.",
//                            Snackbar.LENGTH_LONG
//                        )
//                        snackBar.setAction("Undo") { }
//                        snackBar.show()
//                    } else {}
                    appDatabase.lessonDao().updateLesson(lesson)
                    finish()
                    Toast.makeText(this, "Saqlandi", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }
}