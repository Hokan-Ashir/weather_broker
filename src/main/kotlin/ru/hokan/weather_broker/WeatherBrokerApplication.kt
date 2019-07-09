package ru.hokan.weather_broker

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import com.fasterxml.jackson.databind.util.ClassUtil.getDeclaringClass
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.InjectionPoint
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Scope
import org.springframework.context.support.BeanDefinitionDsl


@SpringBootApplication
class WeatherBrokerApplication

fun main(args: Array<String>) {
    runApplication<WeatherBrokerApplication>(*args)
}
