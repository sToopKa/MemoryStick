package com.sto_opka91.memorystick.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sto_opka91.memorystick.models.Note
import com.sto_opka91.memorystick.models.SingleNote

@Dao
interface NoteDao {
    @Query("SELECT * FROM notes")
    fun getAllNotes(): LiveData<List<Note>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: Note)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSingleNote(note: SingleNote)

    @Query("SELECT * FROM single_notes")
    fun getSingleNote(): LiveData<SingleNote>

    @Query("UPDATE notes SET title = :title, noteText = :note, date = :date, bitmapImage = :uriImage, importance = :importance, createDate = :createDate  WHERE id = :id ")
    suspend fun updateNote(id: Int?, title: String?, note: String?, date: String?, uriImage: ByteArray?, importance: Boolean, createDate:String)

    @Query("UPDATE notes SET bitmapImage = null WHERE id = :id ")
    suspend fun deleteUriImage(id: Int?)

    @Query("DELETE FROM single_notes")
    suspend fun clearNotesTable()

    @Delete
    suspend fun delete (note: Note)

    @Query("SELECT * FROM notes WHERE importance=1")
    fun getImportanceNotes(): LiveData<List<Note>>

    @Query("SELECT * FROM notes WHERE date !=''")
    fun getReminderNotes(): LiveData<List<Note>>

}