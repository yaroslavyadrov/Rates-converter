package revolut.converter.data.datasource.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Rate::class], version = 1, exportSchema = false)
abstract class RatesDatabase : RoomDatabase() {

    abstract fun ratesDao(): RatesDao

    companion object {

        @Volatile
        private var INSTANCE: RatesDatabase? = null

        fun getInstance(context: Context): RatesDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                    context.applicationContext,
                    RatesDatabase::class.java,
                    "Rates.db"
                )
                .build()
    }
}