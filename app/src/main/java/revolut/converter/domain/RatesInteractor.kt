package revolut.converter.domain

import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import revolut.converter.data.repository.RatesRepository
import revolut.converter.domain.model.RateDomain
import revolut.converter.presentation.model.RatePresentation
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class RatesInteractor @Inject constructor(private val ratesRepository: RatesRepository) {

    private var selectedCurrencyRelay = BehaviorRelay.createDefault(
        RatePresentation(
            ratesRepository.getSelectedCurrencyCode(),
            ratesRepository.getSavedAmount().toString()
        )
    )

    private val timer = Observable.interval(1, TimeUnit.SECONDS)

    fun observeRatesData(): Observable<List<RatePresentation>> {
        return Observable.combineLatest(
                timer,
                selectedCurrencyRelay.distinctUntilChanged(),
                BiFunction<Long, RatePresentation, RatePresentation> { _, rate ->
                    rate
                }
            )
            .switchMap { (currency, amount) ->
                ratesRepository.getRates(currency)
                    .map { it.toPresentation(amount.toDouble()) }
                    .toObservable()
            }
    }

    fun newAmount(amount: String) {
        val changedRate =
            selectedCurrencyRelay.value?.copy(amount = amount) ?: throw Exception("dsf")
        ratesRepository.saveAmount(amount.toDouble())
        selectedCurrencyRelay.accept(changedRate)
    }

    fun newCurrencySelectedAsBasic(currencyCode: String, amount: String) {
        ratesRepository.saveAmount(amount.toDouble())
        ratesRepository.saveSelectedCurrencyCode(currencyCode)
        selectedCurrencyRelay.accept(RatePresentation(currencyCode, amount))
    }

    private fun List<RateDomain>.toPresentation(amount: Double): List<RatePresentation> {
        val amountBigDecimal = BigDecimal(amount)
        val decimalZero = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_EVEN)
        return map {
            val rateBigDecimal = BigDecimal(it.rate)
            val result = amountBigDecimal.multiply(rateBigDecimal)
                .setScale(2, RoundingMode.HALF_EVEN)
                .let { bigDecimal ->
                    if (bigDecimal.remainder(BigDecimal.ONE) == decimalZero) {
                        bigDecimal.stripTrailingZeros()
                    } else {
                        bigDecimal
                    }
                }
                .toPlainString()
            if (result == "0.00") {//because of java7 BigDecimal bug
                RatePresentation(it.currencyCode, "0")
            } else {
                RatePresentation(it.currencyCode, result)
            }
        }
    }
}