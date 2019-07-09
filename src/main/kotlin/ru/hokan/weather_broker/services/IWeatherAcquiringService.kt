package ru.hokan.weather_broker.services

import ru.hokan.weather_broker.dto.WeatherResponse

interface IWeatherAcquiringService {
    fun acquireWeatherInfo(city : String) : WeatherResponse?
}