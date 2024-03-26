package com.sto_opka91.memorystick.ui.note_fragment

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.speech.RecognizerIntent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.sto_opka91.memorystick.R
import com.sto_opka91.memorystick.databinding.FragmentNoteBinding
import com.sto_opka91.memorystick.models.Note
import com.sto_opka91.memorystick.models.SingleNote
import com.sto_opka91.memorystick.utils.HelpMethods
import com.sto_opka91.memorystick.utils.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@AndroidEntryPoint
class NoteFragment : Fragment() {

    private var _binding: FragmentNoteBinding? = null
    private val viewModel: NoteViewModel by viewModels()
    private lateinit var listener: DialogNextBtnClickListener
    private val sharedViewModel: SharedViewModel by viewModels()
    private lateinit var voiceInputLauncher: ActivityResultLauncher<Intent>
    private lateinit var singlePhotoPickerLauncher: ActivityResultLauncher<PickVisualMediaRequest>

    private val binding get() = _binding!!
    private var selectedBitmapArray: ByteArray? = null
    private var importance: Boolean = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNoteBinding.inflate(inflater, container, false)
        sharedViewModel.listener.observe(viewLifecycleOwner) { listener ->
            this.listener = listener
        }
        // Инициализация ActivityResultLauncher для голосового ввода
        voiceInputLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // Обработка результатов голосового ввода
                val data: Intent? = result.data
                // Извлечение данных из Intent и обновление массива
                data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.let { results ->
                  binding.editNote.setText(results[0])
                }
            }
        }
         singlePhotoPickerLauncher =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()){uri ->
               uri?.let{
                   binding.uploadImage.visibility = View.VISIBLE
                   val bitmapStart = loadBitmapFromUri(uri)
                   val bitmap = HelpMethods.compressBitmap(bitmapStart!!)
                   binding.uploadImage.setImageBitmap(bitmap)
                   val byteArrayFromImage = HelpMethods.getByteArrayFromBitmap(bitmap)
                   selectedBitmapArray = byteArrayFromImage
               }
            }

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel.listener.observe(viewLifecycleOwner) { listener ->
            // Обработка изменений в listener
            this.listener = listener
        }
        registerEvents()
        initTextsNote()

    }


    private fun initTextsNote() = with(binding) {
        viewModel.singleNote.observe(viewLifecycleOwner) { value ->
            if (value != null) {

                editTitle.setText(value.title)
                editNote.setText(value.noteText)
                edDate.setText(value.date)
                if(value.bitmapImage!=null){
                    imDeleteImage.visibility = View.VISIBLE
                    uploadImage.visibility = View.VISIBLE
                    val bitmap = HelpMethods.getBitmapFromByteArray(value.bitmapImage)
                    uploadImage.setImageBitmap(bitmap)
                    selectedBitmapArray = value.bitmapImage
                }else{
                    uploadImage.visibility = View.GONE
                    imDeleteImage.visibility = View.GONE
                    selectedBitmapArray = null
                }
                if(value.importance){
                    imageImportance.setImageResource(R.drawable.ic_importance)
                    importance = true
                }
            }
        }
        viewModel.selectedDateTime.observe(viewLifecycleOwner) { value ->
            edDate.setText(formatDateTime(value))
        }
    }


    private fun formatDateTime(dateTime: Long): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        return sdf.format(Date(dateTime))
    }

    private fun registerEvents() = with(binding) {
        imDone.setOnClickListener {
            if (editNote.text.toString() != "" || editTitle.text.toString() != "") {
                viewModel.singleNote.observe(viewLifecycleOwner) { value ->
                    if (value != null) {
                        if(selectedBitmapArray!=null){

                            val note = SingleNote(
                                value.id,
                                editTitle.text.toString(),
                                editNote.text.toString(),
                                edDate.text.toString(),
                                selectedBitmapArray,
                                importance,
                                HelpMethods.getCurrentDate()
                            )
                            listener.onSaveTaskSingleNote(note)
                            viewModel.updateNote(note)
                            viewModel.clearTable()
                        }else{
                            val note = SingleNote(
                                value.id,
                                editTitle.text.toString(),
                                editNote.text.toString(),
                                edDate.text.toString(),
                                null,
                                importance,
                                HelpMethods.getCurrentDate()
                            )
                            listener.onSaveTaskSingleNote(note)
                            viewModel.updateNote(note)
                            viewModel.clearTable()
                        }

                    } else {
                        if(selectedBitmapArray!=null){

                            val note = Note(
                                null,
                                editTitle.text.toString(),
                                editNote.text.toString(),
                                edDate.text.toString(),
                                selectedBitmapArray,
                                importance,
                                HelpMethods.getCurrentDate()
                            )
                            listener.onSaveTaskNote(note)
                            viewModel.saveNote(note)
                        }else{
                            val note = Note(
                                null,
                                editTitle.text.toString(),
                                editNote.text.toString(),
                                edDate.text.toString(),
                                null,
                                importance,
                                HelpMethods.getCurrentDate()
                            )
                            listener.onSaveTaskNote(note)
                            viewModel.saveNote(note)
                        }

                    }
                }
                navigateToMain()
            } else {
                Toast.makeText(requireContext(), getString(R.string.note_null), Toast.LENGTH_LONG)
                    .show()
            }

        }
        imBack.setOnClickListener {
            navigateToMain()
        }
        ivPickTime.setOnClickListener {
            viewModel.showDateTimePicker(requireFragmentManager())
        }
        ivVoice.setOnClickListener {
            startSpeachRecongnition()
        }
        imageAddBtn.setOnClickListener {
            singlePhotoPickerLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }
        imageImportance.setOnClickListener {
            if(importance){
                importance=false
                imageImportance.setImageResource(R.drawable.ic_non_importance)
            }else{
                importance=true
                imageImportance.setImageResource(R.drawable.ic_importance)
            }
        }
        imDeleteImage.setOnClickListener {
            viewModel.singleNote.observe(viewLifecycleOwner) { value ->
                if(value!=null){
                    viewModel.deleteUriImage(value.id!!)
                }
                selectedBitmapArray=null
                uploadImage.visibility = View.GONE
                imDeleteImage.visibility = View.GONE
            }
        }
    }

    private fun startSpeachRecongnition() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        voiceInputLauncher.launch(intent)
    }

    private fun navigateToMain() {
        val navController = findNavController()
        navController.navigate(R.id.action_SecondFragment_to_FirstFragment)
    }

    private fun loadBitmapFromUri(uri: Uri): Bitmap? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(requireActivity().contentResolver, uri)
            ImageDecoder.decodeBitmap(source)
        } else {
            MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, uri)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    interface DialogNextBtnClickListener{
        fun onSaveTaskNote(note: Note)
        fun onSaveTaskSingleNote(note: SingleNote)
    }


}