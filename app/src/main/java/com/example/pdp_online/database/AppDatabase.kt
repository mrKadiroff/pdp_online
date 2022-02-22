package com.example.pdp_online.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.pdp_online.dao.KursDao
import com.example.pdp_online.dao.LessonDao
import com.example.pdp_online.dao.ModulDao
import com.example.pdp_online.entity.Kurs
import com.example.pdp_online.entity.Lesson
import com.example.pdp_online.entity.Modul

@Database(entities = [Kurs::class,Modul::class,Lesson::class], version = 1)
abstract class AppDatabase:RoomDatabase() {

    abstract fun kursDao(): KursDao
    abstract fun modulDao(): ModulDao
    abstract fun lessonDao(): LessonDao

    companion object {
        private var instance:AppDatabase? = null

        @Synchronized
        fun getInstance(context: Context):AppDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(context,AppDatabase::class.java,"kurs_db")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
            }
            return instance!!
        }
    }

}