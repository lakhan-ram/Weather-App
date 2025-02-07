package com.example.weather.view.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.weather.BuildConfig
import com.example.weather.R
import com.example.weather.viewmodel.WeatherViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var tvCurrentDate: TextView
    private lateinit var etTextCityName: EditText
    private lateinit var tvCityName: TextView
    private lateinit var ivWeatherImage: ImageView
    private lateinit var tvTemperature: TextView
    private lateinit var tvWeatherCondition: TextView
    private lateinit var cardView: CardView
    private lateinit var tvHumidity: TextView
    private lateinit var tvWindSpeed: TextView
    private lateinit var weatherViewModel: WeatherViewModel

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        tvCurrentDate = findViewById(R.id.tvCurrentDate)
        etTextCityName = findViewById(R.id.etTextCityName)
        tvCityName = findViewById(R.id.tvCityName)
        ivWeatherImage = findViewById(R.id.ivWeatherImage)
        tvTemperature = findViewById(R.id.tvTemperature)
        tvWeatherCondition = findViewById(R.id.tvWeatherCondition)
        cardView = findViewById(R.id.cardView)
        tvHumidity = findViewById(R.id.tvHumidity)
        tvWindSpeed = findViewById(R.id.tvWindSpeed)

        weatherViewModel = ViewModelProvider(this)[WeatherViewModel::class.java]

        weatherViewModel.weatherData.observe(this) { result ->
            result.onSuccess { weatherData ->

                val iconCode = weatherData.weather[0].icon
                val iconUrl = "https://openweathermap.org/img/w/$iconCode.png"
                Glide.with(this).load(iconUrl).into(ivWeatherImage)

                cardView.visibility = View.VISIBLE
                tvCityName.visibility = View.VISIBLE
                tvCityName.text = weatherData.name
                tvTemperature.text = "${(weatherData.main.temp - 273.15).toInt()}°C"
                tvWeatherCondition.text = weatherData.weather[0].description
                tvHumidity.text = "${weatherData.main.humidity}%"
                tvWindSpeed.text = "${weatherData.wind.speed} km/h"

            }
        }


        tvCurrentDate.text = getCurrentDate()

        etTextCityName.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val cityName = etTextCityName.text.toString()
                if (cityName.isNotEmpty()) {
                    weatherViewModel.getWeather(cityName, BuildConfig.apiKey)
                    etTextCityName.text.clear()
                } else {
                    Toast.makeText(this, "Please enter a city name", Toast.LENGTH_SHORT).show()
                }
                true
            } else {
                false
            }
        }

    }

    private fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        return "Today, ${sdf.format(Date())}"
    }
}