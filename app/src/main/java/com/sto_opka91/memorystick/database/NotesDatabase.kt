package com.sto_opka91.memorystick.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sto_opka91.memorystick.models.Note
import com.sto_opka91.memorystick.models.SingleNote

@Database(entities = [Note::class, SingleNote::class], version = 6, exportSchema = false)
abstract class NotesDatabase: RoomDatabase() {

    abstract fun getNoteDao(): NoteDao
}