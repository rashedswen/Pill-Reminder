package com.kwemrj.pillreminder.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kwemrj.pillreminder.data.model.Drug
import com.kwemrj.pillreminder.data.model.Reminders

@Database(entities = [Drug::class, Reminders::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class DrugDatabase : RoomDatabase() {
    abstract fun drugDao(): DrugDao
}