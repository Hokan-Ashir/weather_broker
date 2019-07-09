package ru.hokan.weather_broker.processors

interface IWeatherMessagesProcessor<T> {
    fun processQueueMessage(message : T)
}