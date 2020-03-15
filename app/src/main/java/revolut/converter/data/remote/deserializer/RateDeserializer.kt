package revolut.converter.data.remote.deserializer

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import revolut.converter.data.model.Rate
import java.lang.reflect.Type


class RateDeserializer: JsonDeserializer<Rate> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Rate {
        val jsonObject = json?.asJsonObject
        val currencyCode = jsonObject?.keySet()?.first() ?: ""
        val rate = jsonObject?.get(currencyCode)?.asFloat ?: 0f
        return Rate(currencyCode, rate)
    }
}