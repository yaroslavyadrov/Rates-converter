package revolut.converter.data.repository

import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import revolut.converter.data.datasource.local.db.PositionUpdate
import revolut.converter.data.datasource.local.db.Rate
import revolut.converter.data.datasource.local.db.RatesDao
import revolut.converter.data.datasource.local.preferences.PreferencesHelper
import revolut.converter.data.datasource.remote.RatesApi
import revolut.converter.data.datasource.remote.model.RatesList
import revolut.converter.domain.model.RateDomain
import javax.inject.Inject

class RatesRepository @Inject constructor(
    private val api: RatesApi,
    private val ratesLocalDataSource: RatesDao,
    private val preferences: PreferencesHelper
) {

    fun getRates(currencyCode: String): Single<List<RateDomain>> {
        return Single.zip(
                getRatesFromDb(),
                getRatesFromApi(currencyCode),
                BiFunction<List<Rate>, List<Rate>, List<Rate>> { fromDb, fromApi ->
                    fromApi.map { apiRate ->
                        val position = fromDb.find {
                            it.currencyCode == apiRate.currencyCode
                        }?.positionInList ?: -1
                        apiRate.copy(positionInList = position)
                    }.toMutableList().apply {
                        val selectedRate = Rate(currencyCode, 1.0, 0)
                        ratesLocalDataSource.insert(selectedRate)
                        removeAll { it.currencyCode == currencyCode }
                        add(0, selectedRate)
                    }
                }
            )
            .subscribeOn(Schedulers.io())
            .map(List<Rate>::toDomainRates)
    }

    private fun getRatesFromDb(): Single<List<Rate>> {
        return ratesLocalDataSource.getRates()
            .subscribeOn(Schedulers.io())
    }

    private fun getRatesFromApi(currencyCode: String): Single<List<Rate>> {
        return api.getLatestRates(currencyCode)
            .subscribeOn(Schedulers.io())
            .map { it.copy(currencies = it.currencies.toSortedMap()) }
            .map(RatesList::toDbRates)
            .doOnSuccess { ratesLocalDataSource.upsert(it) }
    }

    fun getSavedAmount() = preferences.amount

    fun saveAmount(amount: Double) {
        preferences.amount = amount
    }

    fun getSelectedCurrencyCode() = preferences.currencyCode

    fun saveSelectedCurrencyCode(currencyCode: String) {
        preferences.currencyCode = currencyCode
    }

    fun updateRatesPositions(indexes: List<Pair<String, Int>>) {
        //fire and forget
        Completable.fromCallable {
                val updates = indexes.map { (currencyCode, position) ->
                    PositionUpdate(
                        currencyCode,
                        position
                    )
                }
                ratesLocalDataSource.updatePositions(updates)
            }
            .subscribeOn(Schedulers.io())
            .subscribe()
    }
}

private fun List<Rate>.toDomainRates(): List<RateDomain> {
    return sortedWith(DbRatesComparator)
        .map { RateDomain(it.currencyCode, it.rate) }
}

private fun RatesList.toDbRates() = currencies.map { Rate(it.key, it.value) }

object DbRatesComparator : Comparator<Rate> {
    override fun compare(o1: Rate, o2: Rate): Int {
        if (o1.positionInList == -1) {
            return 1
        }
        return o1.positionInList - o2.positionInList
    }
}