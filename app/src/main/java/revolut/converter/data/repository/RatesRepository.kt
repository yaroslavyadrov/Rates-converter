package revolut.converter.data.repository

import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import revolut.converter.data.datasource.local.db.RatesDao
import revolut.converter.data.datasource.local.preferences.PreferencesHelper
import revolut.converter.data.datasource.remote.RatesApi
import revolut.converter.data.datasource.remote.model.RatesList
import javax.inject.Inject

class RatesRepository @Inject constructor(
        private val api: RatesApi,
        private val ratesLocalDataSource: RatesDao,
        private val preferences: PreferencesHelper
) {
    fun getLatestRates(currencyCode: String = "EUR"): Single<RatesList> {
        return api.getLatestRates(currencyCode).subscribeOn(Schedulers.io())
    }
}