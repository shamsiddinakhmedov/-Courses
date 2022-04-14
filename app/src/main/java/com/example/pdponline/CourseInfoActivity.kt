package com.example.pdponline

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.pdponline.adapters.CourseInfoAdapter
import com.example.pdponline.databinding.ActivityCourseInfoBinding
import com.example.pdponline.databse.AppDatabase
import com.example.pdponline.databse.CourseWithModule
import com.example.pdponline.databse.Module
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.function.Consumer

class CourseInfoActivity : AppCompatActivity() {
    lateinit var binding: ActivityCourseInfoBinding
    lateinit var appDatabase: AppDatabase
    lateinit var courseInfoAdapter: CourseInfoAdapter

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCourseInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        appDatabase = AppDatabase.getInstance(this)
        val id = intent.getIntExtra("id", 1)
        val course = appDatabase.coursesDao().getCourseById(id)

        binding.textView5.text = course.course.title

        courseInfoAdapter = CourseInfoAdapter(appDatabase,
            course.course.title, course.course.courseIcon,
            object : CourseInfoAdapter.OnMyItemsClickListener {
                override fun itemClick(module: Module) {
                    val intent = Intent(this@CourseInfoActivity, ModuleInfoActivity::class.java)
                    intent.putExtra("mId", module.id)
                    startActivity(intent)
                }
            })
        appDatabase.moduleDao().getAllModulesByOrder(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Consumer<List<Module>>,
                io.reactivex.functions.Consumer<List<Module>> {
                override fun accept(t: List<Module>) {
                    courseInfoAdapter.submitList(t)
                }
            }, object : Consumer<Throwable>, io.reactivex.functions.Consumer<Throwable> {
                override fun accept(t: Throwable) {
                    t.message
                }
            })
        binding.rv.adapter = courseInfoAdapter
    }
}