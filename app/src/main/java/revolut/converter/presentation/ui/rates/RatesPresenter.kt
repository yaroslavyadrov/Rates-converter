package revolut.converter.presentation.ui.rates

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import revolut.converter.domain.RatesInteractor
import revolut.converter.presentation.model.RateItem
import revolut.converter.presentation.model.RatePresentation
import revolut.converter.presentation.ui.base.BasePresenter
import javax.inject.Inject

class RatesPresenter @Inject constructor(private val ratesInteractor: RatesInteractor) :
        BasePresenter<RatesMvpView>() {

    fun onScreenOpen() {
        ratesInteractor.observeRatesData()
                .observeOn(AndroidSchedulers.mainThread())
                .map { it.toListItems() }
                .subscribe { view?.showRatesLst(it) }
                .addTo(disposables)
    }
}

private fun List<RatePresentation>.toListItems(): List<RateItem> {
    return this.map { RateItem(0, it.currencyCode, "123", it.amount) }
}
