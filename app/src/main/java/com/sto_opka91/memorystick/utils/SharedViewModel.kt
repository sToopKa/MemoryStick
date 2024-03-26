package com.sto_opka91.memorystick.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sto_opka91.memorystick.ui.note_fragment.NoteFragment
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor() : ViewModel() {
    private val listenerMutableLiveData: MutableLiveData<NoteFragment.DialogNextBtnClickListener> = MutableLiveData()

    fun setListener(listener: NoteFragment.DialogNextBtnClickListener) {
        listenerMutableLiveData.value = listener
    }

    val listener: LiveData<NoteFragment.DialogNextBtnClickListener> = listenerMutableLiveData
}