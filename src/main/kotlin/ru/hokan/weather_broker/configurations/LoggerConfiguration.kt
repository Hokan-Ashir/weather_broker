package ru.hokan.weather_broker.configurations

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.InjectionPoint
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Scope

@Configuration
class LoggerConfiguration {
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    fun produceLogger(injectionPoint: InjectionPoint): Logger {
        val classOnWired = injectionPoint.member.declaringClass
        return LoggerFactory.getLogger(classOnWired)
    }
}