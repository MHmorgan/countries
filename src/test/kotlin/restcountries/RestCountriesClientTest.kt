package restcountries

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class RestCountriesClientTest {

    lateinit var lastCalledUrl: String

    private fun newClient(json: String): RestCountriesClient {
        val engine = MockEngine { req ->
            lastCalledUrl = req.url.toString()
            respond(
                content = json,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val mock = HttpClient(engine) {
            install(ContentNegotiation) {
                json(Json.Default)
            }
        }

        return RestCountriesClient("https://restcountries.com/v3.1", mock)
    }

    @Test
    fun `getByRegion basic test`() = runTest {
        val json = """
            [
              {
                "name": {"common": "Germany", "official": "Federal Republic of Germany"},
                "currencies": {"EUR": {"name": "Euro", "symbol": "€"}}
              },
              {
                "name": {"common": "France", "official": "French Republic"},
                "currencies": {"EUR": {"name": "Euro", "symbol": "€"}}
              }
            ]
        """.trimIndent()

        val client = newClient(json)
        val actual = client.getByRegion("europe")

        assertThat(lastCalledUrl)
            .isEqualTo("https://restcountries.com/v3.1/region/europe")

        assertThat(actual).hasSize(2)
        assertThat(actual.map { it.name?.common }).containsExactly("Germany", "France")
        assertThat(actual.map { it.name?.official }).containsExactly(
            "Federal Republic of Germany",
            "French Republic"
        )

        client.close()
    }

    @Test
    fun `getAllCurrencies basic test`() = runTest {
        val json = """
            [
              {
                "name": {"common": "Panama", "official": "Republic of Panama"},
                "currencies": {
                  "PAB": {"name": "Panamanian balboa", "symbol": "B/."},
                  "USD": {"name": "United States dollar", "symbol": "$"}
                }
              }
            ]
        """.trimIndent()

        val client = newClient(json)
        val actual = client.getAllCurrencies()

        assertThat(lastCalledUrl)
            .isEqualTo("https://restcountries.com/v3.1/all?fields=currencies,name")

        assertThat(actual).hasSize(1)
        val currencies = actual.first().currencies
        assertThat(currencies).hasSize(2)
        assertThat(currencies?.keys).containsExactlyInAnyOrder("PAB", "USD")
        assertThat(currencies?.get("PAB")?.name).isEqualTo("Panamanian balboa")
        assertThat(currencies?.get("USD")?.name).isEqualTo("United States dollar")

        client.close()
    }
}
