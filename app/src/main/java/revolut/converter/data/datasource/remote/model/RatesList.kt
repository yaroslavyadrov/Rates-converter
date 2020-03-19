package revolut.converter.data.datasource.remote.model

import com.google.gson.annotations.SerializedName

data class RatesList(
        @SerializedName("baseCurrency") val baseCurrency: String,
        @SerializedName("rates") val currencies: Map<String, Float>
)