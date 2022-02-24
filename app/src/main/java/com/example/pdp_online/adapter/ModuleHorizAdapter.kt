package com.example.pdp_online.adapter

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.pdp_online.R
import com.example.pdp_online.databinding.ItemCardBinding
import com.example.pdp_online.databinding.KursListBinding
import com.example.pdp_online.databinding.ModulListBinding
import com.example.pdp_online.entity.Kurs
import com.example.pdp_online.entity.Modul

class ModuleHorizAdapter : ListAdapter<Modul, ModuleHorizAdapter.Vh>(MyDiffUtil()){

    inner class Vh(var itemCardBinding: ItemCardBinding) : RecyclerView.ViewHolder(itemCardBinding.root) {


        fun onBind(modul: Modul) {
            itemCardBinding.text.text = modul.mod_name


            itemCardBinding.knopka.setOnClickListener {
                Toast.makeText(itemCardBinding.root.context, "bosildi", Toast.LENGTH_SHORT).show()
                var bundle = Bundle()
                bundle.putSerializable("horizon",modul)
                Navigation.createNavigateOnClickListener(R.id.darsFragment,bundle).onClick(itemCardBinding.knopka)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemCardBinding.inflate(LayoutInflater.from(parent.context),parent,false))
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


    }

