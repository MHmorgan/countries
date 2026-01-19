import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import restcountries.RestCountriesClient
import kotlin.system.exitProcess

/**
 * Subcommand to fetch and display countries by region.
 * Outputs pretty-printed JSON to stdout.
 */
class RegionCommand(private val client: RestCountriesClient) : CliktCommand(name = "region") {
    private val region: String by argument(help = "The region to query")

    override fun run() {
        try {
            val countries = runBlocking {
                client.getByRegion(region)
            }
            // Output pretty-formatted JSON
            println(prettyJson.encodeToString(countries))
        } catch (e: Exception) {
            echo("Error: ${e.message}", err = true)
            exitProcess(1)
        }
    }
}