import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.main
import com.github.ajalt.clikt.core.subcommands
import kotlinx.serialization.json.Json
import restcountries.RestCountriesClient

val prettyJson = Json { prettyPrint = true }

fun main(args: Array<String>) {
    val client = RestCountriesClient("https://restcountries.com/v3.1")

    try {
        Countries()
            .subcommands(RegionCommand(client))
            .main(args)
    } finally {
        client.close()
    }
}

/**
 * Root command for the Countries CLI application.
 * Acts as a container for subcommands.
 */
class Countries : CliktCommand(name = "countries") {
    override fun run() = Unit
}