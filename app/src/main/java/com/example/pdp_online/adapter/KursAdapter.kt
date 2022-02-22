package com.example.pdp_online.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.pdp_online.databinding.KursListBinding
import com.example.pdp_online.entity.Kurs

class KursAdapter(var onItemClickListener: OnItemClickListener) : ListAdapter<Kurs, KursAdapter.Vh>(MyDiffUtil()){

    inner class Vh(var kursListBinding: KursListBinding) : RecyclerView.ViewHolder(kursListBinding.root) {

        fun onBind(kurs: Kurs) {
            kursListBinding.profile.setImageURI(Uri.parse(kurs.kr_image))
            kursListBinding.headingffffff.text = kurs.kr_name
            kursListBinding.root.setOnClickListener {
                onItemClickListener.onItemClick(kurs)
            }
            kursListBinding.delete.setOnClickListener {
                onItemClickListener.onItemDeleteClick(kurs,position)
            }
            kursListBinding.edit.setOnClickListener {
                onItemClickListener.onItemEditClick(kurs,position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(KursListBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(getItem(position))
    }

    class MyDiffUtil:DiffUtil.ItemCallback<Kurs>(){
        override fun areItemsTheSame(oldItem: Kurs, newItem: Kurs): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Kurs, newItem: Kurs): Boolean {
            return oldItem.equals(newItem)
        }


    }

    interface OnItemClickListener{
        fun onItemClick(kurs: Kurs)
        fun onItemDeleteClick(kurs: Kurs,position: Int)
        fun onItemEditClick(kurs: Kurs,position: Int)
    }

}