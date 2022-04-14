package com.example.pdponline.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pdponline.ModuleInfoActivity
import com.example.pdponline.databinding.MainRvItemBinding
import com.example.pdponline.databse.CourseWithModule
import com.example.pdponline.databse.Module

class MainAdapter(val click: Click): ListAdapter<CourseWithModule, MainAdapter.Vh>(CourseAdapter.MyDiffUtil()) {

    inner class Vh(private val mainRvItemBinding: MainRvItemBinding): RecyclerView.ViewHolder(mainRvItemBinding.root){
        fun onBind(courseWithModule: CourseWithModule) {
            mainRvItemBinding.courseNameTv.text = courseWithModule.course.title
            val moduleAdapter = ModuleAdapter(courseWithModule.modules, object : ModuleAdapter.OnItemClick{
                override fun click(module: Module) {
                    click.moduleClick(module)
                }

            })
            mainRvItemBinding.moduleRv.adapter = moduleAdapter

            mainRvItemBinding.mainLayout.setOnClickListener {
                click.itemClick(courseWithModule)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(MainRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(getItem(position))
    }

    interface Click{
        fun itemClick(courseWithModule: CourseWithModule)
        fun moduleClick(module: Module)
    }
}