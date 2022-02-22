package com.example.pdp_online.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.pdp_online.databinding.KursListBinding
import com.example.pdp_online.databinding.LessonListBinding
import com.example.pdp_online.entity.Kurs
import com.example.pdp_online.entity.Lesson

class LessonAdapter(var kurs: Kurs,var onItemClickListener: OnItemClickListener) : ListAdapter<Lesson, LessonAdapter.Vh>(MyDiffUtil()){

    inner class Vh(var lessonListBinding: LessonListBinding) : RecyclerView.ViewHolder(lessonListBinding.root) {

        fun onBind(lesson: Lesson) {
           lessonListBinding.profile.setImageURI(Uri.parse(kurs.kr_image))
            lessonListBinding.pxotion.text =  "${lesson.lesson_position}-dars"
            lessonListBinding.description.text = lesson.lesson_name

            lessonListBinding.delete.setOnClickListener {
               onItemClickListener.onItemDeleteClick(lesson)
            }
            lessonListBinding.edit.setOnClickListener {
                onItemClickListener.onItemEditClick(lesson)
            }


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(LessonListBinding.inflate(LayoutInflater.from(parent.context),parent,false))
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
        fun onItemDeleteClick(lesson: Lesson)
        fun onItemEditClick(lesson: Lesson)
    }

}