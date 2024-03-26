package com.sto_opka91.memorystick.ui.main_fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sto_opka91.memorystick.models.Note
import com.sto_opka91.memorystick.models.SingleNote
import com.sto_opka91.memorystick.repository.Noterepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository : Noterepository): ViewModel() {
    private val _itemsNote: LiveData<List<Note>> = repository.getAllNotes()
    val itemsNote: LiveData<List<Note>> get() = _itemsNote

    private val _itemsNoteImportance: LiveData<List<Note>> = repository.getAllNotesImportance()
    val itemsNoteImportance: LiveData<List<Note>> get() = _itemsNoteImportance

    private val _itemsNoteReminder: LiveData<List<Note>> = repository.getAllNotesReminder()
    val itemsNoteReminder: LiveData<List<Note>> get() = _itemsNoteReminder



    fun sendNote(note:Note){
        val singleNote = SingleNote(
            note.id,
            note.title,
            note.noteText,
            note.date,
            note.bitmapImage,
            note.importance,
            note.createDate
        )
        viewModelScope.launch {
            repository.insertSingleNote(singleNote)
        }
    }
    fun deleteNote(note:Note){
        viewModelScope.launch {
            repository.deleteNote(note)
        }
    }
    fun clearTable(){
        viewModelScope.launch {
            repository.clearTable()
        }
    }

}