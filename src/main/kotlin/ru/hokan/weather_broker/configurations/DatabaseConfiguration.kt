package ru.hokan.weather_broker.configurations

import de.flapdoodle.embed.process.runtime.Network
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.IOException
import org.springframework.jdbc.datasource.DriverManagerDataSource
import org.springframework.context.annotation.DependsOn
import org.springframework.transaction.annotation.EnableTransactionManagement
import ru.yandex.qatools.embed.postgresql.PostgresProcess
import ru.yandex.qatools.embed.postgresql.PostgresStarter
import ru.yandex.qatools.embed.postgresql.config.AbstractPostgresConfig
import ru.yandex.qatools.embed.postgresql.config.PostgresConfig
import ru.yandex.qatools.embed.postgresql.distribution.Version
import java.lang.String.format
import java.util.*
import javax.sql.DataSource


@Configuration
@EnableTransactionManagement
class DatabaseConfiguration {

    @Value("\${hostName}")
    lateinit var hostName : String

    @Value("\${dbName}")
    lateinit var dbName : String

    @Value("\${userName}")
    lateinit var userName : String

    @Value("\${password}")
    lateinit var password : String

    @Bean
    @DependsOn("postgresProcess")
    fun dataSource(config: PostgresConfig): DataSource {
        val ds = DriverManagerDataSource()
        ds.setDriverClassName(DRIVER_CLASS_NAME)
        ds.url = format("jdbc:postgresql://%s:%s/%s", config.net().host(), config.net().port(), config.storage().dbName())
        ds.username = config.credentials().username()
        ds.password = config.credentials().password()
        return ds
    }

    @Bean
    @Throws(IOException::class)
    fun postgresConfig(): PostgresConfig {
        val postgresConfig = PostgresConfig(Version.V9_3_6,
                AbstractPostgresConfig.Net(hostName, Network.getFreeServerPort()),
                AbstractPostgresConfig.Storage(dbName),
                AbstractPostgresConfig.Timeout(),
                AbstractPostgresConfig.Credentials(userName, password)
        )
        postgresConfig.additionalInitDbParams.addAll(DEFAULT_ADDITIONAL_INIT_DB_PARAMS)
        return postgresConfig
    }

    @Bean(destroyMethod = "stop")
    @Throws(IOException::class)
    fun postgresProcess(config: PostgresConfig): PostgresProcess {
        val runtime = PostgresStarter.getDefaultInstance()
        val exec = runtime.prepare(config)
        return exec.start()
    }

    companion object {
        private val DEFAULT_ADDITIONAL_INIT_DB_PARAMS = listOf("--nosync", "--locale=en_US.UTF-8")
        private const val DRIVER_CLASS_NAME = "org.postgresql.Driver"
    }
}