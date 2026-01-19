import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.int
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import restcountries.CountryDTO
import restcountries.RestCountriesClient
import restcountries.sort
import kotlin.system.exitProcess

class RegionCommand(private val client: RestCountriesClient) : CliktCommand(name = "region") {
    private val log = LoggerFactory.getLogger(javaClass)

    private val region: String by argument(
        help = "The region to query (e.g., europe, africa, americas, asia, oceania)"
    )

    private val json: Boolean by option("--json", help = "Output full JSON instead of table").flag()

    private val desc: Boolean by option("--desc", help = "Sort by name descending (default: ascending)").flag()

    private val limit: Int by option("--limit", help = "Limit the number of results")
        .int()
        .default(0)

    override fun run() {
        try {
            var dtos = runBlocking { client.getByRegion(region) }
                .sort(desc)

            if (limit > 0) dtos = dtos.take(limit)

            when {
                json -> println(prettyJson.encodeToString(dtos))
                else -> printTable(dtos)
            }
        } catch (e: Exception) {
            log.error("Failed to fetch countries by region", e)
            exitProcess(1)
        }
    }

    /**
     * Prints a table with country name and currency using Mordant's awesomeTable.
     */
    private fun printTable(countries: List<CountryDTO>) {
        val table = awesomeTable {
            header {
                row("Country", "Currency")
            }

            body {
                countries.forEach { country ->
                    val name = country.commonName
                        ?: error("Missing a name for the country to display")

                    val currency = country.currencies
                        ?.map { Triple(it.key, it.value.name, it.value.symbol) }
                        ?.joinToString("\n") { (code, name, symbol) ->
                            when (symbol) {
                                null -> "$code: $name"
                                else -> "$code: $name ($symbol)"
                            }
                        }

                    row(name, currency)
                }
            }

            footer {
                row("Total", "${countries.size} countries")
            }
        }
        t.println(table)
    }
}
