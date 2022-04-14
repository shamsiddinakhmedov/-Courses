package com.example.pdponline

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.pdponline.adapters.MainAdapter
import com.example.pdponline.databinding.ActivityMainBinding
import com.example.pdponline.databse.AppDatabase
import com.example.pdponline.databse.CourseWithModule
import com.example.pdponline.databse.Module
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.function.Consumer

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var appDatabase: AppDatabase
    private lateinit var mainAdapter: MainAdapter
    private val TAG = "MainActivity"

    @SuppressLint("SetTextI18n", "CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        appDatabase = AppDatabase.getInstance(this)

        mainAdapter = MainAdapter(object : MainAdapter.Click{
            override fun itemClick(courseWithModule: CourseWithModule) {
                val intent = Intent(this@MainActivity, CourseInfoActivity::class.java)
                intent.putExtra("id", courseWithModule.course.id)
                startActivity(intent)
            }

            override fun moduleClick(module: Module) {
                val intent = Intent(this@MainActivity, ModuleInfoActivity::class.java)
                intent.putExtra("mId", module.id)
                startActivity(intent)
            }
        })

        appDatabase.coursesDao().getAllCourses()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Consumer<List<CourseWithModule>>,
                io.reactivex.functions.Consumer<List<CourseWithModule>> {
                override fun accept(t: List<CourseWithModule>) {
                    mainAdapter.submitList(t)
                }
            }, object : Consumer<Throwable>, io.reactivex.functions.Consumer<Throwable> {
                override fun accept(t: Throwable) {
                    t.message
                }
            },{
                Toast.makeText(this@MainActivity, "Success", Toast.LENGTH_SHORT).show() })
        binding.mainRv.adapter = mainAdapter

        binding.settingsImg.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }
}