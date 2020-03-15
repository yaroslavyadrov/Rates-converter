package revolut.converter.data.model

import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import revolut.converter.data.remote.deserializer.RateDeserializer


data class RatesList(
    @SerializedName("baseCurrency") val baseCurrency: String,
    @SerializedName("rates") val rates: List<Rate>
)

@JsonAdapter(RateDeserializer::class)
data class Rate(
    val currencyCode: String,
    val rate: Float
)