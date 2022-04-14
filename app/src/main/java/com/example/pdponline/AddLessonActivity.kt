package com.example.pdponline

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.pdponline.adapters.LessonAdapter
import com.example.pdponline.databinding.ActivityAddLessonBinding
import com.example.pdponline.databse.AppDatabase
import com.example.pdponline.databse.Lesson
import com.example.pdponline.databse.Module
import com.google.android.material.snackbar.Snackbar
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.function.Consumer

class AddLessonActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddLessonBinding
    lateinit var appDatabase: AppDatabase
    lateinit var lessonAdapter: LessonAdapter
    lateinit var lessonList: ArrayList<Lesson>

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddLessonBinding.inflate(layoutInflater)
        setContentView(binding.root)
        appDatabase = AppDatabase.getInstance(this)
        val moduleId = intent.getIntExtra("module_id", 1)
        val image = intent.getStringExtra("image")
        val module = appDatabase.moduleDao().getModuleById(moduleId)
        lessonList = ArrayList()
        lessonList.addAll(appDatabase.lessonDao().getAllLessonsById(moduleId))
        binding.moduleName.text = module.title

        lessonAdapter =
            LessonAdapter(image!!, object : LessonAdapter.OnMyItemsClickListener {
                override fun editItem(lesson: Lesson) {
                    val intent = Intent(this@AddLessonActivity, EditLessonActivity::class.java)
                    intent.putExtra("lessonId", lesson.id)
                    startActivity(intent)
                }

                override fun removeItem(lesson: Lesson) {
                    val dialog = AlertDialog.Builder(this@AddLessonActivity)
                    dialog.setMessage("Dars o’chishiga rozimisiz?")
                    dialog.setPositiveButton(
                        "Ha"
                    ) { _, which ->
                        appDatabase.lessonDao().deleteLesson(lesson)
                        lessonList.remove(lesson)
                    }
                    dialog.setNegativeButton("Yo'q"
                    ) { dialog, which -> dialog.dismiss() }
                    dialog.show()

                }

            })
        appDatabase.lessonDao().getAllLessons(moduleId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Consumer<List<Lesson>>,
                io.reactivex.functions.Consumer<List<Lesson>> {
                override fun accept(t: List<Lesson>) {
                    lessonAdapter.submitList(t)
                }
            }, object : Consumer<Throwable>, io.reactivex.functions.Consumer<Throwable> {
                override fun accept(t: Throwable) {
                    t.message
                }
            },
                { Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show() })
        binding.rv.adapter = lessonAdapter

        binding.addIcon.setOnClickListener {
            val etLessonName = findViewById<View>(R.id.lesson_name) as EditText
            val strLessonName = etLessonName.text.toString()

            val etLessonDescription = findViewById<View>(R.id.lesson_description) as EditText
            val strLessonDescription = etLessonDescription.text.toString()

            val etLessonNumber = findViewById<View>(R.id.lesson_number) as EditText
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
                    val lesson = Lesson(
                        0,
                        strLessonName,
                        strLessonDescription,
                        strLessonNumber.toInt(),
                        moduleId
                    )

                    var isHave = false
                    for (i in lessonList.indices) {
                        if (strLessonName.trim() == lessonList[i].title.trim() || strLessonNumber == lessonList[i].placeNumber.toString()) {
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
                        Observable.fromCallable {
                            appDatabase.lessonDao().insertLesson(lesson)
                            lessonList.add(lesson)
                        }.subscribe {
                            Toast.makeText(this, "Saqlandi", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

    }
}