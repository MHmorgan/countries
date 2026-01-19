package restcountries

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

/**
 * HTTP client for the REST Countries API.
 *
 * @param apiBase Base URL for the REST Countries API
 * @param client HttpClient instance (defaults to CIO engine with JSON support)
 */
class RestCountriesClient(
    private val apiBase: String,
    private val client: HttpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }
) {

    /**
     * Fetches countries by region from the REST Countries API.
     * @param region The region to query (e.g., "europe", "africa", "americas", "asia", "oceania")
     * @return List of countries in the specified region
     */
    suspend fun getByRegion(region: String): List<CountryDTO> =
        client.get("$apiBase/region/$region").body()

    /**
     * Fetches all countries with only currencies and name fields.
     * @return List of countries with currencies and name data
     */
    suspend fun getAllCurrencies(): List<CountryDTO> =
        client.get("$apiBase/all?fields=currencies,name").body()

    /**
     * Closes the underlying HTTP client and releases resources.
     */
    fun close() {
        client.close()
    }
}