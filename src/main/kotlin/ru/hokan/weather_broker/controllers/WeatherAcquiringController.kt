package ru.hokan.weather_broker.controllers

import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import ru.hokan.weather_broker.dto.WeatherResponse
import org.springframework.amqp.core.AmqpTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestParam
import ru.hokan.weather_broker.dao.PostgreSQLRepository
import ru.hokan.weather_broker.services.IWeatherAcquiringService


@RestController
class WeatherAcquiringController {

    @Autowired
    lateinit var template: AmqpTemplate

    @Autowired
    lateinit var repository : PostgreSQLRepository

    @Autowired
    lateinit var acquiringService: IWeatherAcquiringService

    @Value("\${queueName}")
    lateinit var queueName : String

    @GetMapping("/getWeather", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getWeatherResponse(@RequestParam city : String) : WeatherResponse? {
//        repository.find
        val weatherInfo = acquiringService.acquireWeatherInfo(city)
        template.convertAndSend(queueName, weatherInfo);
        return weatherInfo
    }
}