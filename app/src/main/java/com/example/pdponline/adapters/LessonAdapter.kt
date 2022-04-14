package com.example.pdponline.adapters

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pdponline.databinding.LessonItemBinding
import com.example.pdponline.databse.Lesson

class LessonAdapter(
    val imagePath: String,
    private val onMyItemsClickListener: OnMyItemsClickListener
) : ListAdapter<Lesson, LessonAdapter.Vh>(MyDiffUtil()) {

    inner class Vh(
        private val lessonItemBinding: LessonItemBinding
    ) : RecyclerView.ViewHolder(lessonItemBinding.root) {
        fun onBind(lesson: Lesson) {
            lessonItemBinding.cImg.setImageBitmap(BitmapFactory.decodeFile(imagePath))
            lessonItemBinding.lessonNumber.text = lesson.title
            lessonItemBinding.lessonDescription.text = lesson.description
            lessonItemBinding.editBtn.setOnClickListener {
                onMyItemsClickListener.editItem(lesson)
            }
            lessonItemBinding.removeBtn.setOnClickListener {
                onMyItemsClickListener.removeItem(lesson)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(
            LessonItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(getItem(position))
    }

    class MyDiffUtil : DiffUtil.ItemCallback<Lesson>() {
        override fun areItemsTheSame(oldItem: Lesson, newItem: Lesson): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Lesson, newItem: Lesson): Boolean {
            return oldItem.equals(newItem)
        }

    }

    interface OnMyItemsClickListener {
        fun editItem(lesson: Lesson)
        fun removeItem(lesson: Lesson)
    }
}