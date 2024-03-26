package com.sto_opka91.memorystick.repository


import com.sto_opka91.memorystick.database.NoteDao
import com.sto_opka91.memorystick.models.Note
import com.sto_opka91.memorystick.models.SingleNote
import javax.inject.Inject

class Noterepository @Inject constructor(private val noteDao: NoteDao) {

    fun getAllNotes() = noteDao.getAllNotes()
    fun getAllNotesImportance() = noteDao.getImportanceNotes()
    fun getAllNotesReminder() = noteDao.getReminderNotes()
    fun getSingleNote() = noteDao.getSingleNote()
    suspend fun clearTable() = noteDao.clearNotesTable()
    suspend fun deleteNote(note:Note) = noteDao.delete(note)
    suspend fun updateNote(note: SingleNote) = noteDao.updateNote(note.id, note.title, note.noteText, note.date, note.bitmapImage, note.importance, note.createDate)
    suspend fun deleteUriImage(id: Int) = noteDao.deleteUriImage(id)
    suspend fun insetNote(note: Note) = noteDao.insert(note)
    suspend fun insertSingleNote(note: SingleNote) = noteDao.insertSingleNote(note)
}