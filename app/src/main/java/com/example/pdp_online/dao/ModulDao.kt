package com.example.pdp_online.dao

import androidx.room.*
import com.example.pdp_online.entity.Modul

import io.reactivex.Flowable

@Dao
interface ModulDao {

    @Query("select * from modul")
    fun getAllModule(): Flowable<List<Modul>>

    @Query("select * from modul where mo_kurs=:kursId")
    fun getModuleByKursId(kursId: Int): Flowable<List<Modul>>


    @Insert
    fun addModul(modul: Modul)

    @Delete
    fun deleteModul(modul: Modul)

    @Update
    fun updateModul(modul: Modul)
}