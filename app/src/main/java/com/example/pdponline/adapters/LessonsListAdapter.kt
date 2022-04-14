package com.example.pdponline.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pdponline.LessonInfoActivity
import com.example.pdponline.databinding.LessonsListItemBinding
import com.example.pdponline.databse.Lesson

class LessonsListAdapter(val lessonItemClick: LessonsListAdapter.LessonItemClick, private val lessonsList: List<Lesson>): RecyclerView.Adapter<LessonsListAdapter.Vh>() {
    inner class Vh(private val lessonsListItemBinding: LessonsListItemBinding): RecyclerView.ViewHolder(lessonsListItemBinding.root){
        fun onBind(lesson: Lesson) {
            lessonsListItemBinding.lessonName.text = "${lesson.placeNumber}-dars"

            lessonsListItemBinding.root.setOnClickListener {
                lessonItemClick.itemClick(lesson)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(LessonsListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(lessonsList[position])
    }

    override fun getItemCount(): Int = lessonsList.size

    interface LessonItemClick{
        fun itemClick(lesson: Lesson)
    }
}