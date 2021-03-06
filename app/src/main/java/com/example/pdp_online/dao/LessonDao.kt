package com.example.pdp_online.dao

import androidx.room.*
import com.example.pdp_online.entity.Lesson
import com.example.pdp_online.entity.Modul

import io.reactivex.Flowable

@Dao
interface LessonDao {


    @Query("select * from lesson where dar_modul=:moduleId order by dars_orni")
    fun getLessonByModuleId(moduleId: Int): Flowable<List<Lesson>>

    @Query("select * from lesson")
    fun getAllLesson(): List<Lesson>

    @Insert
    fun addLesson(lesson: Lesson)

    @Delete
    fun deleteLesson(lesson: Lesson)

    @Update
    fun updateLesson(lesson: Lesson)
}