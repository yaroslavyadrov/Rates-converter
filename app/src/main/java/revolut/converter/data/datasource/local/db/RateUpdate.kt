package revolut.converter.data.datasource.local.db

import androidx.room.ColumnInfo

data class RateUpdate(
        @ColumnInfo(name = "currency_code")
        val currencyCode: String,
        @ColumnInfo(name = "rate")
        val rate: Double
)