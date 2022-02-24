package com.example.pdp_online.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
class Lesson :Serializable{
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null

    @ColumnInfo(name = "dars_nomi")
    var lesson_name: String? = null

    @ColumnInfo(name = "dars_malum")
    var lesson_description: String? = null

    @ColumnInfo(name = "dars_orni")
    var lesson_position: Int? = null

    @ColumnInfo(name = "dars_rasm")
    var lesson_image: String? = null

    @ColumnInfo(name = "dar_modul")
    var lesson_modul_id: Int? = null
}