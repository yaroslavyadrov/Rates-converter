package revolut.converter.data.datasource.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface RatesDao {
    @Query("SELECT * FROM rates")
    fun getRates(): Single<List<Rate>>

    @Query("UPDATE rates SET position_in_list = :position WHERE currency_code = :currencyCode")
    fun updatePosition(currencyCode: String, position: Int): Completable

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(rates: List<Rate>): List<Long>

    @Update(onConflict = OnConflictStrategy.REPLACE, entity = Rate::class)
    fun update(rate: RateUpdate)

    @Transaction
    fun upsert(rates: List<Rate>) {
        val rowIDs = insert(rates)
        val weeksToUpdate = rowIDs.mapIndexedNotNull { index, rowID ->
            if (rowID == -1L) null else rates[index]
        }
        weeksToUpdate.forEach { update(RateUpdate(it.currencyCode, it.rate)) }
    }
}