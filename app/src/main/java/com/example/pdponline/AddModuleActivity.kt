package com.example.pdponline

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.example.pdponline.adapters.CourseAdapter
import com.example.pdponline.adapters.LessonAdapter
import com.example.pdponline.adapters.ModuleListAdapter
import com.example.pdponline.databinding.ActivityAddModuleBinding
import com.example.pdponline.databse.*
import com.google.android.material.snackbar.Snackbar
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers
import java.util.function.Consumer

class AddModuleActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddModuleBinding
    lateinit var appDatabase: AppDatabase
    lateinit var moduleListAdapter: ModuleListAdapter

    private lateinit var modulesList: ArrayList<Module>

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddModuleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        appDatabase = AppDatabase.getInstance(this)
        val id = intent.getIntExtra("id", 1)
        val courseWithModule = appDatabase.coursesDao().getCourseById(id)
        binding.courseNameTv.text = courseWithModule.course.title
        val image = courseWithModule.course.courseIcon

        modulesList = ArrayList()
        modulesList.addAll(appDatabase.moduleDao().getModules(id))

        binding.addIcon.setOnClickListener {
            val etModuleName = findViewById<View>(R.id.module_name) as EditText
            val strModuleName = etModuleName.text.toString()

            val etModuleNumber = findViewById<View>(R.id.module_number) as EditText
            val strModuleNumber = etModuleNumber.text.toString()

            when {
                TextUtils.isEmpty(strModuleName.trim()) -> {
                    etModuleName.error = "Bo'sh maydonni to'ldiring"
                }
                TextUtils.isEmpty(strModuleNumber.trim()) -> {
                    etModuleNumber.error = "Bo'sh maydonni to'ldiring"
                }
                else -> {
                    val module = Module(0, strModuleName, strModuleNumber.toInt(), id, image)
                    var isHave = false
                    for (i in modulesList.indices) {
                        if (module.title == modulesList[i].title || module.placeNumber == modulesList[i].placeNumber) {
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
                            appDatabase.moduleDao().insertModule(module)
                            modulesList.add(module)
                        }.subscribe{
                            Toast.makeText(this, "Successfully added", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
        moduleListAdapter = ModuleListAdapter(image, object: ModuleListAdapter.OnMyItemsClickListener{
            override fun itemClick(module: Module) {
                val intent = Intent(this@AddModuleActivity, AddLessonActivity::class.java)
                intent.putExtra("module_id", module.id)
                intent.putExtra("image", image)
                ContextCompat.startActivity(this@AddModuleActivity, intent, Bundle())
            }

            override fun editItem(module: Module) {
                val intent = Intent(this@AddModuleActivity, EditModuleActivity::class.java)
                intent.putExtra("moduleId", module.id)
                startActivity(intent)
            }

            override fun removeItem(module: Module) {
                val dialog = AlertDialog.Builder(this@AddModuleActivity)
                dialog.setMessage("Bu modul ichida darslar kiritilgan. Darslar bilan birgalikda o’chib ketishiga rozimisiz?")
                dialog.setPositiveButton(
                    "Ha"
                ) { _, which ->
                    appDatabase.moduleDao().deleteModule(module)
                    modulesList.remove(module)
                }
                dialog.setNegativeButton("Yo'q"
                ) { dialog, which -> dialog.dismiss() }
                dialog.show()
            }
        })
        appDatabase.moduleDao().getAllModulesByOrder(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Consumer<List<Module>>,
                io.reactivex.functions.Consumer<List<Module>> {
                override fun accept(t: List<Module>) {
                    moduleListAdapter.submitList(t)
                }
            }, object : Consumer<Throwable>, io.reactivex.functions.Consumer<Throwable> {
                override fun accept(t: Throwable) {
                    t.message
                }
            },
                { Toast.makeText(this@AddModuleActivity, "Success", Toast.LENGTH_SHORT).show() })
        binding.rv.adapter = moduleListAdapter

    }
}