package ru.hokan.weather_broker.dao

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import ru.hokan.weather_broker.dto.WeatherResponse

@Repository
@Transactional
interface PostgreSQLRepository : CrudRepository<WeatherResponse, Int> {
    fun findByLocationCurrentObservation(weatherResponse: WeatherResponse, pageable: Pageable): Page<WeatherResponse>
}