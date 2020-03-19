package revolut.converter.domain

import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Observable
import revolut.converter.data.datasource.remote.model.RatesList
import revolut.converter.data.repository.RatesRepository
import revolut.converter.domain.model.Rate
import revolut.converter.util.withLatestFrom
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class RatesInteractor @Inject constructor(private val ratesRepository: RatesRepository) {

    val amountRelay = BehaviorRelay.create<Float>().startWith(100f)
    val selectedCurrencyCodeRelay = BehaviorRelay.create<String>().startWith("EUR")

    fun observeRatesData(): Observable<List<Rate>> {
        return Observable.interval(1, TimeUnit.SECONDS)
                .flatMap { ratesRepository.getLatestRates().toObservable() }
                .map {
                    it.copy(currencies = it.currencies.toSortedMap())
                }
                .withLatestFrom(amountRelay) { rates, amount ->
                    rates.toDomain(amount)
                }
    }

    private fun RatesList.toDomain(amount: Float): List<Rate> {
        return this.currencies.map {
            Rate(it.key, amount * it.value)
        }
    }
}