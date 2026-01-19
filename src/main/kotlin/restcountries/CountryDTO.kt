package restcountries

import kotlinx.serialization.Serializable

@Serializable
data class CountryDTO(
    val name: NameDTO
)

@Serializable
data class NameDTO(
    val common: String,
    val official: String,
    val nativeName: NativeNameDTO
)