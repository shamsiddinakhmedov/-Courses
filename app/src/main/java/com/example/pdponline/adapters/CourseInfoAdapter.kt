package com.example.pdponline.adapters

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pdponline.AddModuleActivity
import com.example.pdponline.databinding.CourseInfoItemBinding
import com.example.pdponline.databinding.CourseItemBinding
import com.example.pdponline.databse.AppDatabase
import com.example.pdponline.databse.CourseWithModule
import com.example.pdponline.databse.Module

class CourseInfoAdapter(val appDatabase: AppDatabase, val string: String, val imagePath: String,  private val onMyItemsClickListener: OnMyItemsClickListener) : ListAdapter<Module, CourseInfoAdapter.Vh>(MyDiffUtil()) {

    inner class Vh(
        private val courseInfoItemBinding: CourseInfoItemBinding,
    ) : RecyclerView.ViewHolder(courseInfoItemBinding.root) {
        fun onBind(module: Module) {
            courseInfoItemBinding.cImg.setImageBitmap(BitmapFactory.decodeFile(imagePath))
            courseInfoItemBinding.moduleName.text = module.title
            val lessons = appDatabase.lessonDao().getAllLessonsById(module.id)
            courseInfoItemBinding.lessonsCount.text = lessons.size.toString()
            courseInfoItemBinding.courseName.text = string
            courseInfoItemBinding.root.setOnClickListener {
                onMyItemsClickListener.itemClick(module)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(CourseInfoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(getItem(position))
    }

    class MyDiffUtil : DiffUtil.ItemCallback<Module>() {
        override fun areItemsTheSame(
            oldItem: Module,
            newItem: Module
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Module,
            newItem: Module
        ): Boolean {
            return oldItem.equals(newItem)
        }
    }

    interface OnMyItemsClickListener {
        fun itemClick(module: Module)
    }

}