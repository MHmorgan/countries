import org.slf4j.LoggerFactory

fun main() {
    val cfg = Config(apiBase = "https://restcountries.com/v3.1")

    val log = LoggerFactory.getLogger("countries")
    log.info("Countries CLI starting...")

    println("Countries CLI starting...")
    println("Countries CLI - Phase 1 Foundation Complete")
}
