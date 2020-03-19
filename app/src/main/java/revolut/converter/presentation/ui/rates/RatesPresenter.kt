package revolut.converter.presentation.ui.rates

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import revolut.converter.domain.RatesInteractor
import revolut.converter.domain.model.Rate
import revolut.converter.presentation.model.RateItem
import revolut.converter.presentation.ui.base.BasePresenter
import javax.inject.Inject

class RatesPresenter @Inject constructor(private val ratesInteractor: RatesInteractor) :
        BasePresenter<RatesMvpView>() {

    fun onScreenOpen() {
        ratesInteractor.observeRatesData()
                .observeOn(AndroidSchedulers.mainThread())
                .map { it.toPresentation() }
                .subscribe { view?.showRatesLst(it) }
                .addTo(disposables)
    }
}

private fun List<Rate>.toPresentation(): List<RateItem> {
    return this.map { RateItem(0, it.currencyCode, "123", it.amount) }
}
