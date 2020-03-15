package revolut.converter.presentation.rates

import revolut.converter.domain.RatesInteractor
import revolut.converter.presentation.base.BasePresenter
import javax.inject.Inject


class RatesPresenter @Inject constructor(private val ratesInteractor: RatesInteractor): BasePresenter<RatesMvpView>() {
}