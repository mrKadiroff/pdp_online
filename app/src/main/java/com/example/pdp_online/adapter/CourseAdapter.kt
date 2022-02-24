package com.example.pdp_online.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pdp_online.R
import com.example.pdp_online.database.AppDatabase
import com.example.pdp_online.databinding.ItemMainBinding
import com.example.pdp_online.databinding.KursListBinding
import com.example.pdp_online.entity.Kurs
import com.example.pdp_online.entity.Modul
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

lateinit var appDatabase: AppDatabase
class CourseAdapter(var onItemClickListener: OnItemClickListener)  : ListAdapter<Kurs, CourseAdapter.Vh>(MyDiffUtil()){

    inner class Vh(var itemMainBinding: ItemMainBinding) : RecyclerView.ViewHolder(itemMainBinding.root) {

        fun onBind(kurs: Kurs) {
            appDatabase = AppDatabase.getInstance(itemMainBinding.root.context)

            itemMainBinding.all.setOnClickListener {
                onItemClickListener.onItemClick(kurs)
            }


           itemMainBinding.tileTv.text = kurs.kr_name

            val moduleHorizAdapter = ModuleHorizAdapter()

            appDatabase.modulDao().getModuleByKursId(kurs.id!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object: Consumer<List<Modul>> {
                    override fun accept(t: List<Modul>?) {
                        moduleHorizAdapter.submitList(t)
                    }

                }, object : Consumer<Throwable> {
                    override fun accept(t: Throwable?) {

                    }

                })

            itemMainBinding.itemsRv.adapter = moduleHorizAdapter
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemMainBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(getItem(position))
    }

    class MyDiffUtil: DiffUtil.ItemCallback<Kurs>(){
        override fun areItemsTheSame(oldItem: Kurs, newItem: Kurs): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Kurs, newItem: Kurs): Boolean {
            return oldItem.equals(newItem)
        }


    }
    interface OnItemClickListener{
        fun onItemClick(kurs: Kurs)
    }



}