package com.example.pdponline

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.pdponline.databinding.ActivityEditModuleBinding
import com.example.pdponline.databse.AppDatabase
import com.example.pdponline.databse.Module
import com.google.android.material.snackbar.Snackbar

class EditModuleActivity : AppCompatActivity() {
    lateinit var binding: ActivityEditModuleBinding
    lateinit var appDatabase: AppDatabase
    lateinit var modulesList: ArrayList<Module>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditModuleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        appDatabase = AppDatabase.getInstance(this)
        val id = intent.getIntExtra("moduleId", 1)
        val module = appDatabase.moduleDao().getModuleById(id)
        val courseId = module.coursesOwnerId

        modulesList = ArrayList()
        modulesList.addAll(appDatabase.moduleDao().getModules(courseId))

        binding.courseNameTv.text = module.title
        binding.mName.setText(module.title)
        binding.mNumber.setText(module.placeNumber.toString())

        binding.checkIcon.setOnClickListener {
            val etModuleName = findViewById<View>(R.id.mName) as EditText
            val strModuleName = etModuleName.text.toString()

            val etModuleNumber = findViewById<View>(R.id.mNumber) as EditText
            val strModuleNumber = etModuleNumber.text.toString()

            when {
                TextUtils.isEmpty(strModuleName.trim()) -> {
                    etModuleName.error = "Bo'sh maydonni to'ldiring"
                }
                TextUtils.isEmpty(strModuleNumber.trim()) -> {
                    etModuleNumber.error = "Bo'sh maydonni to'ldiring"
                }
                else -> {
                    var isHave = false
                    for (i in modulesList.indices) {
                        if (strModuleName == modulesList[i].title || strModuleNumber == modulesList[i].placeNumber.toString()) {
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
                        module.title = strModuleName
                        module.placeNumber = strModuleNumber.toInt()
                        appDatabase.moduleDao().updateModule(module)
                        Toast.makeText(this, "Saqlandi", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }
        }
    }
}