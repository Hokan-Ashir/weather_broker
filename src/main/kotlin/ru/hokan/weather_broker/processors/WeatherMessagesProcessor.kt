package ru.hokan.weather_broker.processors

import org.slf4j.Logger
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import ru.hokan.weather_broker.dao.PostgreSQLRepository
import ru.hokan.weather_broker.dto.WeatherResponse


@Service
class WeatherMessagesProcessor : IWeatherMessagesProcessor<WeatherResponse> {

    @Autowired
    lateinit var logger: Logger
    
    @Autowired
    lateinit var repository: PostgreSQLRepository

    @Value("\${queueName}")
    lateinit var queueName: String

    @RabbitListener(queues = ["\${queueName}"])
    override fun processQueueMessage(message: WeatherResponse) {
        logger.info("Received from $queueName: $message")
        repository.save(message)
    }
}