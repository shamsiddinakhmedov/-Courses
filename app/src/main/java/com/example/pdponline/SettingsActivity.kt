package com.example.pdponline

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.example.pdponline.adapters.CourseAdapter
import com.example.pdponline.databinding.ActivitySettingsBinding
import com.example.pdponline.databse.AppDatabase
import com.example.pdponline.databse.CourseWithModule
import com.example.pdponline.databse.Courses
import com.github.florent37.runtimepermission.kotlin.askPermission
import com.google.android.material.snackbar.Snackbar
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.io.FileOutputStream
import java.util.function.Consumer

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var appDatabase: AppDatabase
    private lateinit var courseAdapter: CourseAdapter
    private lateinit var coursesList: ArrayList<Courses>

    var imagePath: String? = null

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        appDatabase = AppDatabase.getInstance(this)
        coursesList = ArrayList()
        coursesList.addAll(appDatabase.coursesDao().getAllC())

        binding.courseImageShape.setOnClickListener {
            askPermission(
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) {
                getImageContent.launch("image/*")
                //all permissions already granted or just granted
            }.onDeclined { e ->
                if (e.hasDenied()) {

                    AlertDialog.Builder(this)
                        .setMessage("Please accept our permissions")
                        .setPositiveButton("yes") { dialog, which ->
                            e.askAgain()
                        } //ask again
                        .setNegativeButton("no") { dialog, which ->
                            dialog.dismiss()
                        }
                        .show()
                }

                if (e.hasForeverDenied()) {
                    Toast.makeText(this, "ForeverDenied", Toast.LENGTH_SHORT).show()
                    // you need to open setting manually if you really need it
                    e.goToSettings()
                }
            }
        }

        binding.settingsImg.setOnClickListener {
            val etCourseName = findViewById<View>(R.id.course_name) as EditText
            val strCourseName = etCourseName.text.toString()

            when {
                TextUtils.isEmpty(strCourseName.trim()) -> {
                    etCourseName.error = "Bo'sh maydonni to'ldiring"
                }
                else -> {
                    if (imagePath != null) {
                        val courses = Courses(0, strCourseName, imagePath!!)
                        var isHave = false
                        for (i in coursesList.indices) {
                            if (courses.title.trim().uppercase() == coursesList[i].title.trim().uppercase()) {
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
                            Observable.fromCallable{
                                appDatabase.coursesDao().insertCourse(courses)
                                coursesList.add(courses)
                            }.subscribe{
                                Toast.makeText(this, "Successfully added", Toast.LENGTH_SHORT).show()
                                binding.courseName.setText("")
                                binding.courseImage.setImageResource(R.drawable.photo)
                            }
                        }
                    } else {
                        val snackBar =
                            Snackbar.make(it, "Bo'sh maydonni to'ldiring.", Snackbar.LENGTH_LONG)
                        snackBar.setAction("Undo") { }
                        snackBar.show()
                    }
                }
            }
        }

        courseAdapter = CourseAdapter(
            object : CourseAdapter.OnMyItemsClickListener {
            override fun itemClick(courseWithModule: CourseWithModule) {
                val intent = Intent(this@SettingsActivity, AddModuleActivity::class.java)
                intent.putExtra("id", courseWithModule.course.id)
                ContextCompat.startActivity(this@SettingsActivity, intent, Bundle())
            }

            override fun editItem(courseWithModule: CourseWithModule) {
                val intent = Intent(this@SettingsActivity, EditActivity::class.java)
                intent.putExtra("id", courseWithModule.course.id)
                startActivity(intent)
            }

            override fun removeItem(courseWithModule: CourseWithModule) {
                val dialog = AlertDialog.Builder(this@SettingsActivity)
                dialog.setMessage("Bu kurs ichida modullar kiritilgan. Modullar bilan birgalikda o’chib ketishiga rozimisiz?")
                dialog.setPositiveButton(
                    "Ha"
                ) { _, which ->
                    appDatabase.coursesDao().deleteCourse(courseWithModule.course)
                    coursesList.remove(courseWithModule.course)
                }
                dialog.setNegativeButton("Yo'q"
                ) { dialog, which -> dialog.dismiss() }
                dialog.show()
            }
        })
        appDatabase.coursesDao().getAllCourses()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Consumer<List<CourseWithModule>>,
                io.reactivex.functions.Consumer<List<CourseWithModule>> {
                override fun accept(t: List<CourseWithModule>) {
                    courseAdapter.submitList(t)
                }
            }, object : Consumer<Throwable>, io.reactivex.functions.Consumer<Throwable> {
                override fun accept(t: Throwable) {
                    t.message
                }
            }, object : Action {
                override fun run() {
                    Toast.makeText(this@SettingsActivity, "Success", Toast.LENGTH_SHORT).show()
                }
            })
        binding.rv.adapter = courseAdapter

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