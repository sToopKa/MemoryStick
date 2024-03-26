package com.sto_opka91.memorystick.di_room


import com.sto_opka91.memorystick.repository.TimePickerHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.util.Calendar
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TimeModule {

    @Provides
    @Singleton
    fun provideCalendar(): Calendar {
        return Calendar.getInstance()
    }

    @Provides
    @Singleton
    fun provideTimePickerHelper(calendar: Calendar): TimePickerHelper {
        return TimePickerHelper(calendar)
    }
}