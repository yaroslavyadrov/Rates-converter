package revolut.converter.data.datasource.local.db

import androidx.room.*
import io.reactivex.Single

@Dao
interface RatesDao {
    @Query("SELECT * FROM rates")
    fun getRates(): Single<List<Rate>>

    @Update(entity = Rate::class)
    fun updatePositions(newPositions: List<PositionUpdate>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(rates: List<Rate>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(rate: Rate): Long

    @Update(entity = Rate::class)
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