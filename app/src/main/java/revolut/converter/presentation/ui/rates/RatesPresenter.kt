package revolut.converter.presentation.ui.rates

import com.mynameismidori.currencypicker.ExtendedCurrency
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import revolut.converter.R
import revolut.converter.domain.RatesInteractor
import revolut.converter.presentation.model.RateItem
import revolut.converter.presentation.model.RatePresentation
import revolut.converter.presentation.ui.base.BasePresenter
import javax.inject.Inject

class RatesPresenter @Inject constructor(
    private val ratesInteractor: RatesInteractor
) : BasePresenter<RatesMvpView>() {

    var firstItemCode = ""

    fun startObserveRates() {
        view?.showLoading()
        ratesInteractor.observeRatesData()
            .observeOn(AndroidSchedulers.mainThread())
            .map { it.toListItems() }
            .subscribe(
                {
                    val needScrollToTop = it.first().currencyCode != firstItemCode
                    firstItemCode = it.first().currencyCode
                    view?.showRatesList(it, needScrollToTop)
                },
                { view?.showError() }
            )
            .addTo(disposables)
    }

    fun onAmountChanged(newAmount: String) {
        ratesInteractor.newAmount(newAmount)
    }

    fun onRateItemClick(selectedCurrencyCode: String, amount: String) {
        ratesInteractor.newCurrencySelectedAsBasic(selectedCurrencyCode, amount)
    }

    private fun List<RatePresentation>.toListItems(): List<RateItem> {
        return this.map {
            val currency = ExtendedCurrency.getCurrencyByISO(it.currencyCode)

            val textColor = if (it.amount == "0") {
                R.color.gray
            } else {
                R.color.black
            }
            RateItem(currency.flag, it.currencyCode, currency.name, it.amount, textColor)
        }
    }
}

