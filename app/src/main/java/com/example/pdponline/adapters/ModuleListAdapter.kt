package com.example.pdponline.adapters

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pdponline.AddLessonActivity
import com.example.pdponline.databinding.LessonItemBinding
import com.example.pdponline.databinding.ModuleListItemBinding
import com.example.pdponline.databse.CourseWithModule
import com.example.pdponline.databse.Lesson
import com.example.pdponline.databse.Module

class ModuleListAdapter(
    val imagePath: String, private val onMyItemsClickListener: OnMyItemsClickListener
) : ListAdapter<Module, ModuleListAdapter.Vh>(MyDiffUtil()) {

    inner class Vh(
        private val moduleListItemBinding: ModuleListItemBinding,
    ) : RecyclerView.ViewHolder(moduleListItemBinding.root) {
        fun onBind(module: Module, position: Int) {
            moduleListItemBinding.cImg.setImageBitmap(BitmapFactory.decodeFile(imagePath))
            moduleListItemBinding.moduleNumber.text = module.placeNumber.toString()
            moduleListItemBinding.textView4.text = module.title

            moduleListItemBinding.root.setOnClickListener {
                onMyItemsClickListener.itemClick(module)
            }
            moduleListItemBinding.editBtn.setOnClickListener {
                onMyItemsClickListener.editItem(module)
            }
            moduleListItemBinding.removeBtn.setOnClickListener {
                onMyItemsClickListener.removeItem(module)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(
            ModuleListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(getItem(position), position)
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
        fun editItem(module: Module)
        fun removeItem(module: Module)
    }

}