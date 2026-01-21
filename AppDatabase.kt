package com.example.myapplication
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [HolidayEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun holidayDao(): HolidayDao
}
