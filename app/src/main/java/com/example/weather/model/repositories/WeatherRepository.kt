package com.example.weather.model.repositories

import com.example.weather.model.api.RetrofitInstance
import com.example.weather.model.entities.WeatherData

class WeatherRepository {

    suspend fun getWeather(city: String, apiKey: String): WeatherData {
        val result = RetrofitInstance.weatherApi.getWeather(city, apiKey)
        return result
    }
}