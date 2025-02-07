package com.example.weather.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weather.model.entities.WeatherData
import com.example.weather.model.repositories.WeatherRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WeatherViewModel: ViewModel() {

    private val repository = WeatherRepository()
    private val _weatherData = MutableLiveData<Result<WeatherData>>()
    val weatherData: LiveData<Result<WeatherData>> = _weatherData

    fun getWeather(city: String, apiKey: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = repository.getWeather(city, apiKey)
            _weatherData.postValue(Result.success(result))
        }
    }
}