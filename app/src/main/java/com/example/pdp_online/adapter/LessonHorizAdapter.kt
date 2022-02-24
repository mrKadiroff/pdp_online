package com.example.pdp_online.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.pdp_online.databinding.DarsListBinding
import com.example.pdp_online.databinding.KursListBinding
import com.example.pdp_online.databinding.LessonListBinding
import com.example.pdp_online.entity.Kurs
import com.example.pdp_online.entity.Lesson

class LessonHorizAdapter(var onItemClickListener: OnItemClickListener) : ListAdapter<Lesson, LessonHorizAdapter.Vh>(MyDiffUtil()){

    inner class Vh(var darsListBinding: DarsListBinding) : RecyclerView.ViewHolder(darsListBinding.root) {

        fun onBind(lesson: Lesson) {
            darsListBinding.raqam.text = lesson.lesson_position.toString()

            darsListBinding.root.setOnClickListener {
                onItemClickListener.onItemClick(lesson)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(DarsListBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(getItem(position))
    }

    class MyDiffUtil:DiffUtil.ItemCallback<Lesson>(){
        override fun areItemsTheSame(oldItem: Lesson, newItem: Lesson): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Lesson, newItem: Lesson): Boolean {
            return oldItem.equals(newItem)
        }


    }

    interface OnItemClickListener{
        fun onItemClick(lesson: Lesson)
    }

}