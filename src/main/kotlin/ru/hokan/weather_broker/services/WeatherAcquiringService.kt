package ru.hokan.weather_broker.services

import org.slf4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import ru.hokan.weather_broker.dto.WeatherResponse
import java.net.URLEncoder
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec


@Service
class WeatherAcquiringService : IWeatherAcquiringService {

    @Value("\${url}")
    lateinit var requestingURL : String

    @Value("\${appId}")
    lateinit var appId : String

    @Value("\${consumerKey}")
    lateinit var consumerKey : String

    @Value("\${consumerSecret}")
    lateinit var consumerSecret : String

    @Autowired
    lateinit var logger : Logger

    override fun acquireWeatherInfo(city: String): WeatherResponse? {
        val timestamp = Date().time / 1000
        val nonce = ByteArray(32)
        val rand = Random()
        rand.nextBytes(nonce)
        val oauthNonce = String(nonce).replace("\\W".toRegex(), "")

        val parameters = ArrayList<String>()
        parameters.add("oauth_consumer_key=$consumerKey")
        parameters.add("oauth_nonce=$oauthNonce")
        parameters.add("oauth_signature_method=$signatureMethod")
        parameters.add("oauth_timestamp=$timestamp")
        parameters.add("oauth_version=1.0")
        parameters.add("location=" + URLEncoder.encode(city, "UTF-8"))
        parameters.add("format=json")
        parameters.sort()

        val parametersList = StringBuffer()
        for (i in parameters.indices) {
            parametersList.append((if (i > 0) "&" else "") + parameters[i])
        }

        val signatureString = "GET&" +
                URLEncoder.encode(requestingURL, "UTF-8") + "&" +
                URLEncoder.encode(parametersList.toString(), "UTF-8")

        val signature: String?
        try {
            val signingKey = SecretKeySpec("$consumerSecret&".toByteArray(), hasingAlgorithm)
            val mac = Mac.getInstance(hasingAlgorithm)
            mac.init(signingKey)
            val rawHMAC = mac.doFinal(signatureString.toByteArray())
            val encoder = Base64.getEncoder()
            signature = encoder.encodeToString(rawHMAC)
        } catch (e: Exception) {
            logger.error("Unable to append signature")
            return null
        }

        val authorizationLine = "OAuth " +
                "oauth_consumer_key=\"" + consumerKey + "\", " +
                "oauth_nonce=\"" + oauthNonce + "\", " +
                "oauth_timestamp=\"" + timestamp + "\", " +
                "oauth_signature_method=$signatureMethod, " +
                "oauth_signature=\"" + signature + "\", " +
                "oauth_version=\"1.0\""

        val restTemplate = RestTemplate()
        val headers = HttpHeaders()
        headers.set(HttpHeaders.AUTHORIZATION, authorizationLine)
        headers.set(yahooAppIdHeaderName, appId)
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        val entity = HttpEntity<WeatherResponse>(headers)
        val url = "$requestingURL?$locationQueryParameter=$city&$formatQueryParameter=$jsonFormatParameterValue"
        val response = restTemplate.exchange(url, HttpMethod.GET, entity, WeatherResponse::class.java)
        return response.body
    }

    companion object {
        private const val hasingAlgorithm = "HmacSHA1"
        private const val signatureMethod = "HMAC-SHA1"
        private const val yahooAppIdHeaderName = "X-Yahoo-App-Id"
        private const val locationQueryParameter = "location"
        private const val formatQueryParameter = "format"
        private const val jsonFormatParameterValue = "json"
    }
}