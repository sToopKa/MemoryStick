package com.sto_opka91.memorystick.di_room

import android.content.Context
import androidx.room.Room
import com.sto_opka91.memorystick.database.NoteDao
import com.sto_opka91.memorystick.database.NotesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Provides
    @Singleton
    fun provideArticleDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context,
            NotesDatabase::class.java,
            "note_database"
        ).fallbackToDestructiveMigration().build()
    @Provides
    fun provideNoteDao(appDatabase: NotesDatabase):NoteDao{
        return appDatabase.getNoteDao()
    }


}