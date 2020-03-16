package revolut.converter.presentation.ui.rates

import revolut.converter.domain.RatesInteractor
import revolut.converter.presentation.ui.base.BasePresenter
import javax.inject.Inject


class RatesPresenter @Inject constructor(private val ratesInteractor: RatesInteractor): BasePresenter<RatesMvpView>() {

    fun onScreenOpen() {
        ratesInteractor.observeRatesData()

    }
}