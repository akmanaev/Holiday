package com.example.myapplication
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HolidayDao {
    @Query("SELECT * FROM holidays WHERE date = :date AND country = :country")
    suspend fun getHolidays(date: String, country: String): List<HolidayEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveHolidays(holidays: List<HolidayEntity>)
}
