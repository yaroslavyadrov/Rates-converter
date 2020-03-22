package revolut.converter.domain

import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Observable
import revolut.converter.data.repository.RatesRepository
import revolut.converter.domain.model.RateDomain
import revolut.converter.presentation.model.RatePresentation
import revolut.converter.util.withLatestFrom
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class RatesInteractor @Inject constructor(private val ratesRepository: RatesRepository) {

    val amountRelay = BehaviorRelay.create<Double>()
            .startWith(ratesRepository.getSavedAmount())
            .doOnNext { ratesRepository.saveAmount(it) }

    val selectedCurrencyCodeRelay = BehaviorRelay.create<String>()
            .startWith(ratesRepository.getSelectedCurrencyCode())
            .doOnNext { ratesRepository.saveSelectedCurrencyCode(it) }

    fun observeRatesData(): Observable<List<RatePresentation>> {
        return Observable.interval(1, TimeUnit.SECONDS)
                .flatMap { selectedCurrencyCodeRelay }
                .flatMap { ratesRepository.getRates(it).toObservable() }
                .withLatestFrom(amountRelay) { rates, amount ->
                    rates.toPresentation(amount)
                }
    }

    private fun List<RateDomain>.toPresentation(amount: Double): List<RatePresentation> {
        val amountBigDecimal = BigDecimal(amount)
        return map {
            val rateBigDecimal = BigDecimal(it.rate)
            val result = amountBigDecimal.multiply(rateBigDecimal)
                    .setScale(2, RoundingMode.HALF_EVEN)
                    .toString()
            RatePresentation(it.currencyCode, result)
        }
    }
}