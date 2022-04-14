package com.example.pdponline

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.pdponline.databinding.ActivityEditBinding
import com.example.pdponline.databse.AppDatabase
import com.example.pdponline.databse.CourseWithModule
import com.example.pdponline.databse.Courses
import com.google.android.material.snackbar.Snackbar
import io.reactivex.Observable
import java.io.File
import java.io.FileOutputStream
import java.util.function.BiFunction

class EditActivity : AppCompatActivity() {
    lateinit var binding: ActivityEditBinding
    lateinit var appDatabase: AppDatabase
    lateinit var coursesList: ArrayList<Courses>
    var imagePath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val id = intent.getIntExtra("id", 1)
        appDatabase = AppDatabase.getInstance(this)
        coursesList = ArrayList()
        coursesList.addAll(appDatabase.coursesDao().getAllC())

        val courses = appDatabase.coursesDao().getCourseById(id)
        imagePath = courses.course.courseIcon

        binding.textView2.text = courses.course.title
        binding.courseImage.setImageBitmap(BitmapFactory.decodeFile(courses.course.courseIcon))
        binding.cName.setText(courses.course.title)

        binding.courseImageShape.setOnClickListener {
            getImageContent.launch("image/*")
        }

        binding.checkImg1.setOnClickListener {
            val etCourseName = findViewById<View>(R.id.cName) as EditText
            val strCourseName = etCourseName.text.toString()

            when {
                TextUtils.isEmpty(strCourseName.trim()) -> {
                    etCourseName.error = "Bo'sh maydonni to'ldiring"
                }
                else -> {
                    var isHave = false
                    for (i in coursesList.indices) {
                        if (strCourseName == coursesList[i].title.trim().uppercase()
                        ) {
                            isHave = true
                            break
                        }
                    }
                    if (isHave) {
                        val snackBar = Snackbar.make(
                            it,
                            "Bunday kurs ro'yxatda mavjud.",
                            Snackbar.LENGTH_LONG
                        )
                        snackBar.setAction("Undo") { }
                        snackBar.show()
                    } else {
                        courses.course.title = strCourseName
                        courses.course.courseIcon = imagePath!!
                        appDatabase.coursesDao().updateCourse(courses.course)
                        finish()
                        Toast.makeText(this, "Saqlandi", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
    }

    //gallery
    private var getImageContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri ?: return@registerForActivityResult
            binding.courseImage.setImageURI(uri)

            val openInputSystem = contentResolver.openInputStream(uri)
            val file = File(filesDir, "image_${openInputSystem.hashCode()}.jpg")
            val outputStream = FileOutputStream(file)
            openInputSystem?.copyTo(outputStream)
            openInputSystem?.close()
            outputStream.close()
            imagePath = file.path
        }
}