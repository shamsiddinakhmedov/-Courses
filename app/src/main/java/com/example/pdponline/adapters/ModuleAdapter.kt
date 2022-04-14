package com.example.pdponline.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pdponline.databinding.ModuleItemBinding
import com.example.pdponline.databse.Module

class ModuleAdapter(private val moduleList: List<Module>, val onItemClick: OnItemClick): RecyclerView.Adapter<ModuleAdapter.Vh>() {

    inner class Vh(private val moduleItemBinding: ModuleItemBinding): RecyclerView.ViewHolder(moduleItemBinding.root){
        fun onBind(module: Module) {
            moduleItemBinding.moduleNameTv.text = module.title
            moduleItemBinding.root.setOnClickListener {
                onItemClick.click(module)
            }

        }


    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ModuleItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }
    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(moduleList[position])
    }
    override fun getItemCount(): Int = moduleList.size
    interface OnItemClick{
        fun click(module: Module)
    }
}
