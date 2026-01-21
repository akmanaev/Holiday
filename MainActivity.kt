package com.example.myapplication
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CalendarView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var tvDate: TextView
    private lateinit var progressBar: ProgressBar
    private val apiService = ApiClient.apiService
    private val apiKey = "Your API key should be here."

    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        try {
            db = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java,
                "holidays.db"
            ).build()
        }catch (e: Exception){
            Log.e("Db", "ошибка работи: $e")
            finish()
        }
        recyclerView = findViewById(R.id.rvHolidays)
        tvDate = findViewById(R.id.tvDate)
        progressBar = findViewById(R.id.progressBar)

        val calendarView = findViewById<CalendarView>(R.id.calendarView)
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            loadHolidays(year, month + 1, dayOfMonth)
        }
    }
    private fun updateUI(holidays: List<Holiday>) {
            recyclerView.layoutManager = LinearLayoutManager(applicationContext)
            recyclerView.adapter = HolidayAdapter(holidays)
    }
    private fun loadHolidays(year: Int, month: Int, day: Int) {
        tvDate.text = "$day.$month.$year"
        val dateStr = String.format("%04d-%02d-%02d", year, month, day)

        lifecycleScope.launch {
                progressBar.visibility = View.VISIBLE

            val cachedHolidays = db.holidayDao().getHolidays(dateStr, "RU")
            if (cachedHolidays.isNotEmpty()) { // поменял тут
                val holidayList = cachedHolidays.map { entity ->
                    Holiday(entity.name, entity.description)
                }
                updateUI(holidayList)
                tvDate.text = "$day.$month.$year (из кэша)"
                return@launch
            }
            if (isNetworkAvailable()) {
                try {
                    val response = apiService.getHolidays(apiKey, "RU", year, month, day)
                    val holidays = response.response.holidays
                    if (!holidays.isEmpty()) { //поменял тут
                        val roomHolidays = holidays.map {
                            HolidayEntity(
                                id = "${dateStr}_RU",
                                name = it.name,
                                description = it.description,
                                date = dateStr,
                                country = "RU"
                            )
                        }
                        db.holidayDao().saveHolidays(roomHolidays)

                        updateUI(holidays)
                    } else {
                        withContext(Dispatchers.Main) {
                            recyclerView.adapter = null
                            tvDate.text = "$day.$month.$year \nНет праздников на эту дату"
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        tvDate.text = "$day.$month.$year \nОшибка загрузки: ${e.message}"
                    }
                }
            } else {
                withContext(Dispatchers.Main) {
                    recyclerView.adapter = null
                    tvDate.text = "$day.$month.$year \nНет интернета. Проверьте подключение."
                }
            }

            withContext(Dispatchers.Main) {
                progressBar.visibility = View.GONE
            }
        }
    }
    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}
