package com.example.pdp_online.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
class Modul : Serializable{
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null

    @ColumnInfo(name = "mo_nomi")
    var mod_name: String? = null

    @ColumnInfo(name = "mo_orni")
    var mod_position: Int? = null

    @ColumnInfo(name = "mo_rasm")
    var mod_image: String? = null

    @ColumnInfo(name = "mo_kurs")
    var mod_course: Int? = null
}