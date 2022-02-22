package com.example.pdp_online.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.pdp_online.databinding.KursListBinding
import com.example.pdp_online.databinding.ModulListBinding
import com.example.pdp_online.entity.Kurs
import com.example.pdp_online.entity.Modul

class ModuleAdapter(var kurs: Kurs,var onItemClickListener: OnItemClickListener) : ListAdapter<Modul, ModuleAdapter.Vh>(MyDiffUtil()){

    inner class Vh(var modulListBinding: ModulListBinding) : RecyclerView.ViewHolder(modulListBinding.root) {

        fun onBind(modul: Modul) {
         modulListBinding.headingffffff.text = modul.mod_name
            modulListBinding.profile.setImageURI(Uri.parse(kurs.kr_image))
            modulListBinding.moduleSize.text = modul.mod_position

            modulListBinding.delete.setOnClickListener {
                onItemClickListener.onItemDeleteClick(modul)
            }
            modulListBinding.edit.setOnClickListener {
                onItemClickListener.onItemEditClick(modul)
            }
            modulListBinding.root.setOnClickListener {
                onItemClickListener.onItemClick(modul)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ModulListBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(getItem(position))
    }

    class MyDiffUtil:DiffUtil.ItemCallback<Modul>(){
        override fun areItemsTheSame(oldItem: Modul, newItem: Modul): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Modul, newItem: Modul): Boolean {
            return oldItem.equals(newItem)
        }

    }
    interface OnItemClickListener{
        fun onItemClick(modul: Modul)
        fun onItemDeleteClick(modul: Modul)
        fun onItemEditClick(modul: Modul)
    }



    }

