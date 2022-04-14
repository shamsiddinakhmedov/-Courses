package com.example.pdponline

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.pdponline.adapters.LessonsListAdapter
import com.example.pdponline.databinding.ActivityModuleInfoBinding
import com.example.pdponline.databse.AppDatabase
import com.example.pdponline.databse.Lesson

class ModuleInfoActivity : AppCompatActivity() {
    lateinit var binding: ActivityModuleInfoBinding
    lateinit var appDatabase: AppDatabase
    lateinit var lessonsListAdapter: LessonsListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityModuleInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        appDatabase = AppDatabase.getInstance(this)

        val id = intent.getIntExtra("mId", 1)
        val module = appDatabase.moduleDao().getModuleById(id)
        binding.moduleName.text = module.title
        val lessons = appDatabase.lessonDao().getAllLessonsById(module.id)
        lessonsListAdapter = LessonsListAdapter(object: LessonsListAdapter.LessonItemClick{
            override fun itemClick(lesson: Lesson) {
                val intent = Intent(this@ModuleInfoActivity, LessonInfoActivity::class.java)
                intent.putExtra("lessonId", lesson.id)
                startActivity(intent)
            }

        }, lessons)

        binding.rv.adapter = lessonsListAdapter
    }
}