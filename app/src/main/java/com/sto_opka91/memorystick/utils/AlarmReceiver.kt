package com.sto_opka91.memorystick.utils

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.sto_opka91.memorystick.R
import com.sto_opka91.memorystick.models.Note
import com.sto_opka91.memorystick.ui.main_fragment.MainFragment


    class AlarmReceiver: BroadcastReceiver() {

        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        @SuppressLint("MissingPermission")
        override fun onReceive(context: Context?, intent: Intent?) {
            val note = intent?.getSerializableExtra("note", Note::class.java)
            Log.d("myLog", note?.title!!)

            val i = Intent(context, MainFragment::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            val pendingIntent = PendingIntent.getActivity(context,
                note.id.hashCode(),i, PendingIntent.FLAG_IMMUTABLE)

            val builder = NotificationCompat.Builder(context!!, "todoApp")
                .setSmallIcon(R.drawable.ic_alarm)
                .setContentTitle(note.title)
                .setContentText(note.noteText)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)

            val notificationManager = NotificationManagerCompat.from(context)
            notificationManager.notify(note.id.hashCode(), builder.build())
        }
    }
