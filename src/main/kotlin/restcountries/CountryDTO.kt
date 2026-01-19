package restcountries

import kotlinx.serialization.Serializable

/**
 * Data transfer object for the country information retrieved from the REST Countries API.
 *
 * All fields are nullable to support requests with any combination of fields.
 */
@Serializable
data class CountryDTO(
    val name: NameDTO? = null,
    val cca2: String? = null,
    val cca3: String? = null,
    val independent: Boolean? = null,
    val status: String? = null,
    val unMember: Boolean? = null,
    val currencies: Map<String, CurrencyDTO>? = null,
    val idd: IddDTO? = null,
    val capital: List<String>? = null,
    val altSpellings: List<String>? = null,
    val region: String? = null,
    val subregion: String? = null,
    val languages: Map<String, String>? = null,
    val latlng: List<Double>? = null,
    val landlocked: Boolean? = null,
    val area: Double? = null,
    val demonyms: DemonymsDTO? = null,
    val translations: Map<String, TranslationDTO>? = null,
    val flag: String? = null,
    val maps: MapsDTO? = null,
    val population: Long? = null,
    val car: CarDTO? = null,
    val timezones: List<String>? = null,
    val continents: List<String>? = null,
    val flags: FlagsDTO? = null,
    val coatOfArms: CoatOfArmsDTO? = null,
    val startOfWeek: String? = null,
    val capitalInfo: CapitalInfoDTO? = null,
    val postalCode: PostalCodeDTO? = null,
    val borders: List<String>? = null
) {
    val commonName: String?
        get() = name?.common

}

fun Iterable<CountryDTO>.sort(desc: Boolean) = when {
    desc -> sortedByDescending { it.commonName }
    else -> sortedBy { it.commonName }
}

// -----------------------------------------------------------------------------
//
// Nested DTOs
//
// -----------------------------------------------------------------------------

@Serializable
data class NameDTO(
    val common: String,
    val official: String,
    val nativeName: Map<String, NativeNameDTO>? = null
)

@Serializable
data class NativeNameDTO(
    val official: String,
    val common: String
)

@Serializable
data class CurrencyDTO(
    val name: String,
    val symbol: String? = null
)

@Serializable
data class IddDTO(
    val root: String? = null,
    val suffixes: List<String>? = null
)

@Serializable
data class DemonymsDTO(
    val eng: DemonymDTO? = null,
    val fra: DemonymDTO? = null
)

@Serializable
data class DemonymDTO(
    val f: String,
    val m: String
)

@Serializable
data class TranslationDTO(
    val official: String,
    val common: String
)

@Serializable
data class MapsDTO(
    val googleMaps: String,
    val openStreetMaps: String
)

@Serializable
data class CarDTO(
    val signs: List<String>? = null,
    val side: String
)

@Serializable
data class FlagsDTO(
    val png: String,
    val svg: String,
    val alt: String? = null
)

@Serializable
data class CoatOfArmsDTO(
    val png: String? = null,
    val svg: String? = null
)

@Serializable
data class CapitalInfoDTO(
    val latlng: List<Double>? = null
)

@Serializable
data class PostalCodeDTO(
    val format: String? = null,
    val regex: String? = null
)
