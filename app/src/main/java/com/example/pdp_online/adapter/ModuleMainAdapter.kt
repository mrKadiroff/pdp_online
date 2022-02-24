package com.example.pdp_online.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.pdp_online.databinding.KursListBinding
import com.example.pdp_online.databinding.MainModulListBinding
import com.example.pdp_online.databinding.ModulListBinding
import com.example.pdp_online.entity.Kurs
import com.example.pdp_online.entity.Modul

class ModuleMainAdapter(var kurs: Kurs, var onItemClickListener: OnItemClickListener) : ListAdapter<Modul, ModuleMainAdapter.Vh>(MyDiffUtil()){

    inner class Vh(var modulListBinding: MainModulListBinding) : RecyclerView.ViewHolder(modulListBinding.root) {

        fun onBind(modul: Modul) {
            modulListBinding.profile.setImageURI(Uri.parse(kurs.kr_image))
            modulListBinding.headingffffff.text = modul.mod_name
            modulListBinding.moduleSize.text = modul.mod_position.toString()
            modulListBinding.kursss.text = kurs.kr_name
            modulListBinding.batafsil.setOnClickListener {
                onItemClickListener.onItemClick(modul)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(MainModulListBinding.inflate(LayoutInflater.from(parent.context),parent,false))
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
    }



    }

