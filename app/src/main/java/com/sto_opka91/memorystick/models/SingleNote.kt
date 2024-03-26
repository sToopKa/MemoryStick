package com.sto_opka91.memorystick.models


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "single_notes")
data class SingleNote(
    @PrimaryKey(autoGenerate = true)
    val id:Int?,
    val title: String?,
    val noteText: String?,
    val date: String?,
    val bitmapImage: ByteArray?,
    val importance: Boolean,
    val createDate: String
)
