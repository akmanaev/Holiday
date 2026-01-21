package com.example.myapplication

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "holidays")
data class HolidayEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val date: String,
    val country: String
)

data class Holiday(
    val name: String,
    val description: String
)

data class HolidayResponse(
    val response: ResponseData
)

data class ResponseData(
    val holidays: List<Holiday>
)
