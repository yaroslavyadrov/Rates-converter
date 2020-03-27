package revolut.converter.data.datasource.local.db

import androidx.room.ColumnInfo


data class PositionUpdate(
    @ColumnInfo(name = "currency_code")
    val currencyCode: String,
    @ColumnInfo(name = "position_in_list")
    val rate: Int
)