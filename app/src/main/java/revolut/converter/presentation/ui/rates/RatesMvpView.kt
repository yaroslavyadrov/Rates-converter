package revolut.converter.presentation.ui.rates

import revolut.converter.presentation.model.RateItem
import revolut.converter.presentation.ui.base.MvpView

interface RatesMvpView : MvpView {
    fun showRatesList(list: List<RateItem>, needScrollToTop: Boolean)
    fun showLoading()
    fun showError()
}