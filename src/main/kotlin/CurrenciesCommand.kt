import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import restcountries.RestCountriesClient
import kotlin.system.exitProcess

/**
 * Command to fetch and display all countries with their currencies.
 * Uses the API endpoint: all?fields=currencies,name
 */
class CurrenciesCommand(private val client: RestCountriesClient) : CliktCommand(name = "currencies") {
    private val log = LoggerFactory.getLogger(javaClass)

    private val json: Boolean by option("--json", help = "Output full JSON instead of table").flag()

    private val desc: Boolean by option("--desc", help = "Sort by name descending (default: ascending)").flag()

    override fun run() {
        try {
            val dtos = runBlocking { client.getAllCurrencies() }

            val curToCntry = dtos
                .mapNotNull { dto -> dto.currencies?.map { it.key to dto.commonName!! } }
                .flatten()
                .groupBy({ it.first }, { it.second} )

            val cmp = when {
                desc -> Comparator.reverseOrder<String>()
                else -> Comparator.naturalOrder<String>()
            }

            when {
                json -> println(prettyJson.encodeToString(curToCntry))
                else -> printTable(curToCntry.toSortedMap(cmp))
            }
        } catch (e: Exception) {
            log.error("Failed to fetch currencies", e)
            exitProcess(1)
        }
    }

    /**
     * Prints a table with country name and currency using Mordant's awesomeTable.
     */
    private fun printTable(curToCntry: Map<String, List<String>>) {
        val table = awesomeTable {
            header {
                row("Currency", "Countries")
            }

            body {
                curToCntry.forEach { (currency, countries) ->
                    val cs = countries
                        .sorted()
                        .joinToString("\n")
                    row(currency, cs)
                }
            }

            footer {
                row("Total", "${curToCntry.size} currencies")
            }
        }
        t.println(table)
    }
}
