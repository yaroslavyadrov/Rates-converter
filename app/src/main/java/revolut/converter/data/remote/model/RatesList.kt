package revolut.converter.data.remote.model

import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import revolut.converter.data.remote.deserializer.RateDeserializer


data class RatesList(
    @SerializedName("baseCurrency") val baseCurrency: String,
    @SerializedName("rates") val currencies: List<Currency>
)

@JsonAdapter(RateDeserializer::class)
data class Currency(
    val currencyCode: String,
    val rate: Float
)