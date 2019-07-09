package ru.hokan.weather_broker.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class WeatherResponse(@Id @JsonIgnore val id : Int, val location: Location, val currentObservation: CurrentObservation, val forecasts: Forecasts)

@Entity
data class Wind(@Id @JsonIgnore val id : Int, val chill: Int, val direction: Int, val speed: Float)

@Entity
data class Location(@Id @JsonIgnore val id : Int, val woeId: Int, val city: String, val region: String, val country: String, val lat: Float, val long: Float, val timezoneId: String)

@Entity
data class Forecasts(@Id @JsonIgnore val id : Int, val forecasts: List<Forecast>)

@Entity
data class Forecast(@Id @JsonIgnore val id : Int, val day : String, val date : Int, val low : Int, val high : Int, val text : String, val code : Int)

@Entity
data class CurrentObservation(@Id @JsonIgnore val id : Int, val wind : Wind, val atmosphere: Atmosphere, val astronomy: Astronomy, val condition: Condition, val pubDate : Int)

@Entity
data class Condition(@Id @JsonIgnore val id : Int, val text : String, val code : Int, val temperature : Int)

@Entity
data class Atmosphere(@Id @JsonIgnore val id : Int, val humidity : Int, val visibility : Float, val pressure : Float, val rising : Int)

@Entity
data class Astronomy(@Id @JsonIgnore val id : Int, val sunrise : String, val sunset : String)