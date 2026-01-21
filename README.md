# Holiday Calendar
The application displays the holidays on the selected date using an online API and a local data warehouse (Room DB). If there is no internet connection, it displays cached data or notifies you when you need to connect.
---
## Key functions
### 1. Date selection
- Through CalendarView, the user selects a day/month/year.
- When you select a date, the download of holidays starts automatically.
### 2. Getting data from the network
- The Calendarific API is used (https://calendarific.com/api/).
- Requests are executed via Retrofit with coroutines (suspend functions).
- Authentication requires an API key (in the code, is shortened as "Your API key should be here").
### 3. Local storage
- The data is saved in the Room Database (holidays).
- The holidays table contains:
  - id (unique key: date + country),
  - name (name of the holiday),
  - description (description),
  - date (string in the YYYY-MM-DD format),
  - country (country code, for example, "RU").
### 4. Caching and offline mode
- Before making a request to the network, the local database is checked.
- If the data is in the cache, it is displayed instantly.
- If not, it is loaded from the API and saved to the database.
### 5. Network error handling
- The Internet connection is checked via ConnectivityManager.
- If there is no network, the message is displayed: "No Internet connection. Check the connection."
- API errors are handled with the output of a message in the TextView.
### 6. Displaying results
- Holidays are displayed in RecyclerView with a custom adapter (HolidayAdapter).
- For each holiday, the following are shown:
  - Name (tvName),
  - Description (tvDescription).

## The structure of the code
### MainActivity.kt
- The basic logic:
  - Initialization of the Room DB.
  - Date selection processing in CalendarView.
  - Data download (network + cache).
  - Updating the UI via updateUI().
### HolidayDao.kt
- Interface for working with databases:
  - getHolidays() — request by date and country.
  - saveHolidays() — saves a list of holidays.
### HolidayApiService.kt
- Defining API methods via Retrofit:
  - getHolidays() is a GET request to /v2/holidays.
### HolidayAdapter.kt
- Adapter for RecyclerView:
  - Binding data to list items.
### Holiday.kt
- Data classes:
  - HolidayEntity is an entity for a Room.
  - Holiday is the model to display.
  - HolidayResponse and responseData are structures for parsing JSON.
### AppDatabase.kt
- Abstract class for Room DB:
  - Contains a holidayDao().
### ApiClient.kt
- A loner (object) for working with Retrofit:
  - Configuring the base URL and the Gson converter.
    
## How to launch
1. Replace "Your API key should be here" with the real key from Calendarific.
2. Build the project in Android Studio.
3. Install the app on your device/emulator.
4. Select a date in the calendar — the application will show the holidays (if any).
