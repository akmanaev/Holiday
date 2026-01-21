package com.example.myapplication

import retrofit2.http.GET
import retrofit2.http.Query

interface HolidayApiService {

    @GET("v2/holidays")
    suspend fun getHolidays(
        @Query("api_key") apiKey: String,
        @Query("country") country: String,
        @Query("year") year: Int,
        @Query("month") month: Int,
        @Query("day") day: Int
    ): HolidayResponse
}