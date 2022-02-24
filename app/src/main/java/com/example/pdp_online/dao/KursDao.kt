package com.example.pdp_online.dao

import androidx.room.*
import com.example.pdp_online.entity.Kurs
import io.reactivex.Flowable

@Dao
interface KursDao {

    @Query("select * from kurs")
    fun getAllKurs(): Flowable<List<Kurs>>

    @Query("select * from kurs")
    fun getAllCourse(): List<Kurs>

    @Insert
    fun addKurs(kurs: Kurs)

    @Delete
    fun deleteKurs(kurs: Kurs)

    @Update
    fun updateKurs(kurs: Kurs)
}