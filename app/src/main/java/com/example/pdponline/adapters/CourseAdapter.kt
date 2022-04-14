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
import com.example.pdponline.databinding.CourseItemBinding
import com.example.pdponline.databse.CourseWithModule

class CourseAdapter(private val onMyItemsClickListener: OnMyItemsClickListener) : ListAdapter<CourseWithModule, CourseAdapter.Vh>(MyDiffUtil()) {

    inner class Vh(
        private val courseItemBinding: CourseItemBinding,
    ) : RecyclerView.ViewHolder(courseItemBinding.root) {
        fun onBind(courseWithModule: CourseWithModule) {
            courseItemBinding.cImg.setImageBitmap(BitmapFactory.decodeFile(courseWithModule.course.courseIcon))
            courseItemBinding.textView4.text = courseWithModule.course.title
            courseItemBinding.root.setOnClickListener {
                onMyItemsClickListener.itemClick(courseWithModule)
            }
            courseItemBinding.editBtn.setOnClickListener {
                onMyItemsClickListener.editItem(courseWithModule)
            }
            courseItemBinding.removeBtn.setOnClickListener {
                onMyItemsClickListener.removeItem(courseWithModule)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(
            CourseItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(getItem(position))
    }

    class MyDiffUtil : DiffUtil.ItemCallback<CourseWithModule>() {
        override fun areItemsTheSame(
            oldItem: CourseWithModule,
            newItem: CourseWithModule
        ): Boolean {
            return oldItem.course.id == newItem.course.id
        }

        override fun areContentsTheSame(
            oldItem: CourseWithModule,
            newItem: CourseWithModule
        ): Boolean {
            return oldItem.equals(newItem)
        }
    }

    interface OnMyItemsClickListener {
        fun itemClick(courseWithModule: CourseWithModule)
        fun editItem(courseWithModule: CourseWithModule)
        fun removeItem(courseWithModule: CourseWithModule)
    }

}