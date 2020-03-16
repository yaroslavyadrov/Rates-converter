package revolut.converter.data.remote.deserializer

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import revolut.converter.data.remote.model.Currency
import java.lang.reflect.Type


class RateDeserializer: JsonDeserializer<Currency> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Currency {
        val jsonObject = json?.asJsonObject
        val currencyCode = jsonObject?.keySet()?.first() ?: ""
        val rate = jsonObject?.get(currencyCode)?.asFloat ?: 0f
        return Currency(currencyCode, rate)
    }
}