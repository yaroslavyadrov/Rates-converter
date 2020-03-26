package revolut.converter.data.datasource.local.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rates")
data class Rate(
    @PrimaryKey
    @ColumnInfo(name = "currency_code")
    val currencyCode: String,
    @ColumnInfo(name = "rate")
    val rate: Double,
    @ColumnInfo(name = "position_in_list")
    val positionInList: Int = -1
)

