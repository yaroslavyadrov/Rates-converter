package revolut.converter.data.datasource.local.db

import androidx.room.*
import io.reactivex.Single

@Dao
interface RatesDao {
    @Query("SELECT * FROM rates")
    fun getRates(): Single<List<Rate>>

    @Query("UPDATE rates SET position_in_list = :position WHERE currency_code = :currencyCode")
    fun updatePosition(currencyCode: String, position: Int)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(rates: List<Rate>): List<Long>

    @Update(onConflict = OnConflictStrategy.REPLACE, entity = Rate::class)
    fun update(rate: RateUpdate)

    @Transaction
    fun upsert(rates: List<Rate>) {
        val rowIDs = insert(rates)
        val ratesToUpdate = rowIDs.mapIndexedNotNull { index, rowID ->
            if (rowID == -1L) rates[index] else null
        }
        ratesToUpdate.forEach { update(RateUpdate(it.currencyCode, it.rate)) }
    }
}