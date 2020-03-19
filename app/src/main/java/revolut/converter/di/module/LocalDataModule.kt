package revolut.converter.di.module

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import revolut.converter.data.datasource.local.db.RatesDao
import revolut.converter.data.datasource.local.db.RatesDatabase
import revolut.converter.data.datasource.local.preferences.PreferencesHelper
import revolut.converter.data.datasource.local.preferences.PreferencesHelperImpl
import javax.inject.Singleton

@Module
open class LocalDataModule constructor(private val app: Application) {

    companion object {
        const val PREF_FILE_NAME = "revolut_rates_preferences"
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(): SharedPreferences {
        return app.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun providePreferencesHelper(preferences: SharedPreferences): PreferencesHelper {
        return PreferencesHelperImpl(preferences)
    }

    @Provides
    @Singleton
    fun provideRatesDataSource(): RatesDao {
        return RatesDatabase.getInstance(app).ratesDao()
    }
}