package com.sto_opka91.memorystick.ui.main_fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.tabs.TabLayout
import com.sto_opka91.memorystick.R
import com.sto_opka91.memorystick.databinding.FragmentMainBinding
import com.sto_opka91.memorystick.models.Note
import com.sto_opka91.memorystick.models.SingleNote
import com.sto_opka91.memorystick.ui.note_fragment.NoteFragment
import com.sto_opka91.memorystick.utils.AlarmReceiver
import com.sto_opka91.memorystick.utils.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

@AndroidEntryPoint
class MainFragment : Fragment(), MainRcAdapter.NotesItemClickListener, NoteFragment.DialogNextBtnClickListener{

    private var _binding: FragmentMainBinding? = null
    private val viewModel: MainViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()

    private lateinit var adapterNote : MainRcAdapter


    private lateinit var alarmManager: AlarmManager
    private lateinit  var pendingIntent: PendingIntent
    private lateinit var pLauncher: ActivityResultLauncher<Array<String>>


    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentMainBinding.inflate(inflater, container, false)

        return binding.root

    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnInit()
        initAdapter()
        initDateForAdapter()
        createNotificationChannel()
        registerPermissionListener()
        checkPostPermission()
        sharedViewModel.setListener(this@MainFragment)
        initTabLayout()
    }

    private fun initDateForAdapter() {
        viewModel.itemsNote.observe(viewLifecycleOwner){list ->

            adapterNote.submitList(list)
        }
        viewModel.clearTable()
    }
    private fun initTabLayout()=with(binding){
        tbNotes.addTab(binding.tbNotes.newTab().setText(getString(R.string.tab_1)))
        tbNotes.addTab(binding.tbNotes.newTab().setText(R.string.tab_2))
        tbNotes.addTab(binding.tbNotes.newTab().setText(R.string.tab_3))

        tbNotes.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let { selectedTab ->
                    filterListByTabPosition(selectedTab.position)
                }
                // Обработка выбора вкладки
                when (tab?.position) {
                    0 -> {
                        viewModel.itemsNote.observe(viewLifecycleOwner){list ->
                            adapterNote.submitList(list)
                        }
                        viewModel.clearTable()
                    }
                    1 -> {
                        viewModel.itemsNoteImportance.observe(viewLifecycleOwner){list ->
                            adapterNote.submitList(list)
                        }
                        viewModel.clearTable()
                    }

                    2 ->  {
                        viewModel.itemsNoteReminder.observe(viewLifecycleOwner){list ->
                            adapterNote.submitList(list)
                        }
                        viewModel.clearTable()
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // Ничего не делаем
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // Ничего не делаем
            }
        })
    }

    private fun filterListByTabPosition(position: Int) {

    }

    private fun btnInit()= with(binding){
        fBtnInsNote.setOnClickListener {
            navigateToNoteFragment()
        }
        sView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    when(tbNotes.selectedTabPosition){
                        0 -> {
                            val filteredList = viewModel.itemsNote.value?.filter { note ->
                                note.title!!.contains(newText, ignoreCase = true) ||
                                        note.noteText!!.contains(newText, ignoreCase = true)
                            }
                            adapterNote.submitList(filteredList)
                        }
                        1 -> {
                            val filteredList = viewModel.itemsNoteImportance.value?.filter { note ->
                                note.title!!.contains(newText, ignoreCase = true) ||
                                        note.noteText!!.contains(newText, ignoreCase = true)
                            }
                            adapterNote.submitList(filteredList)
                        }
                        else -> {
                            val filteredList = viewModel.itemsNoteReminder.value?.filter { note ->
                                note.title!!.contains(newText, ignoreCase = true) ||
                                        note.noteText!!.contains(newText, ignoreCase = true)
                            }
                            adapterNote.submitList(filteredList)
                        }
                    }

                }
                return true
            }
        })
    }
    private fun initAdapter(){
        adapterNote = MainRcAdapter(this)
        binding.rcNote.adapter = adapterNote
        binding.rcNote.setHasFixedSize(true)
        binding.rcNote.layoutManager = StaggeredGridLayoutManager(1, LinearLayout.VERTICAL)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun navigateToNoteFragment(){
        val navController = findNavController()
        navController.navigate(R.id.action_FirstFragment_to_SecondFragment)
    }
    override fun onItemClick(note: Note) {
        viewModel.sendNote(note)
        navigateToNoteFragment()
    }

    override fun onLongItemClicked(note: Note) {
        viewModel.deleteNote(note)
    }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun checkPostPermission(){
        when{
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS)
                    == PackageManager.PERMISSION_GRANTED ->{
            }
            shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) ->{
                Toast.makeText(context, getString(R.string.toast_permission), Toast.LENGTH_SHORT).show()
            }
            else ->{
                pLauncher.launch(arrayOf(Manifest.permission.POST_NOTIFICATIONS))
            }
        }
    }
    private fun registerPermissionListener(){
        pLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){
        }
    }


    private fun createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val name: CharSequence = "ToDoReminderChannel"
            val description = "Channel for alarmManager"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("todoApp", name, importance)
            channel.description = description
            val notificationManager: NotificationManager = requireActivity().getSystemService(
                Context.NOTIFICATION_SERVICE
            ) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun setAlarm(note:Note) {

        if(note.date!=""){
            alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
            pendingIntent = Intent(context, AlarmReceiver::class.java).let{ intent ->
                intent.putExtra("note",note)
                PendingIntent.getBroadcast(context,note.id.hashCode(),intent,PendingIntent.FLAG_IMMUTABLE)
            }
            val calendar: Calendar = Calendar.getInstance()
            calendar.time = changeDateString(note.date!!)
            alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        }
    }
    private fun cancelAlarm(noteId: Int) {
        alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent =
            PendingIntent.getBroadcast(context, noteId.hashCode(), intent, PendingIntent.FLAG_IMMUTABLE)
        alarmManager.cancel(pendingIntent)
        pendingIntent.cancel()
    }

    @SuppressLint("SimpleDateFormat")
    private fun changeDateString(date:String): Date {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm")
        val dateCalendar: Date = sdf.parse(date)!!
        return dateCalendar
    }

    override fun onSaveTaskNote(note: Note) {
        setAlarm(note)
    }

    override fun onSaveTaskSingleNote(note: SingleNote) {
        cancelAlarm(note.id!!)
        val noteNote = Note(note.id, note.title, note.noteText, note.date, note.bitmapImage, note.importance, note.createDate)
        setAlarm(noteNote)
    }

}