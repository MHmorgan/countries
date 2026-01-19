package restcountries

import Config
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

/**
 * HTTP client for the REST Countries API.
 * Uses Ktor with CIO engine and kotlinx.serialization for JSON parsing.
 */
class RestCountriesClient(private val cfg: Config) {

    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                // Ignore unknown keys for forward API compatibility
                ignoreUnknownKeys = true
            })
        }
    }

    /**
     * Fetches countries by region from the REST Countries API.
     * @param region The region to query (e.g., "europe", "africa", "americas", "asia", "oceania")
     * @return List of countries in the specified region
     */
    suspend fun getByRegion(region: String): List<CountryDTO> =
        client.get("${cfg.apiBase}/region/$region").body()

    /**
     * Closes the underlying HTTP client and releases resources.
     */
    fun close() {
        client.close()
    }
}
