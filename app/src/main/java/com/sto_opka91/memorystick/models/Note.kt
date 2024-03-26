package com.sto_opka91.memorystick.models


import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "notes")
data class Note (

    @PrimaryKey(autoGenerate = true)
    val id:Int?,
    val title: String?,
    val noteText: String?,
    val date: String?,
    val bitmapImage: ByteArray?,
    val importance: Boolean,
    val createDate: String
    ): Serializable
