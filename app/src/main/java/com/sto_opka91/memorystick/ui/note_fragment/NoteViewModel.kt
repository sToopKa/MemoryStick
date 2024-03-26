package com.sto_opka91.memorystick.ui.note_fragment

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sto_opka91.memorystick.models.Note
import com.sto_opka91.memorystick.models.SingleNote
import com.sto_opka91.memorystick.repository.Noterepository
import com.sto_opka91.memorystick.repository.TimePickerHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val repository : Noterepository,
    private val timePickerHelper: TimePickerHelper
): ViewModel() {
    private val _singleNote: LiveData<SingleNote> = repository.getSingleNote()
    val singleNote: LiveData<SingleNote> get() = _singleNote

    private val _selectedDateTime = MutableLiveData<Long>()
    val selectedDateTime: LiveData<Long> get() = _selectedDateTime


    fun saveNote(note: Note){
        viewModelScope.launch {
            repository.insetNote(note)
        }
    }
    fun updateNote(note: SingleNote){
        viewModelScope.launch {
            repository.updateNote(note)
        }
    }

    fun deleteUriImage(id: Int){
        viewModelScope.launch {
            repository.deleteUriImage(id)
        }
    }

    fun clearTable(){
        viewModelScope.launch {
            repository.clearTable()
        }
    }

    fun showDateTimePicker(fragmentManager: FragmentManager) {
        timePickerHelper.setFragmentManager(fragmentManager)
        timePickerHelper.showDateTimePicker { selectedDateTimeInMillis ->
            _selectedDateTime.value = selectedDateTimeInMillis
        }
    }

}