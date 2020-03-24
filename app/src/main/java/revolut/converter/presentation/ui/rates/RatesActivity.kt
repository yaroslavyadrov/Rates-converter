package revolut.converter.presentation.ui.rates

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import revolut.converter.R
import revolut.converter.presentation.model.RateItem
import revolut.converter.presentation.ui.base.BaseActivity
import revolut.converter.util.bindView
import javax.inject.Inject


class RatesActivity : BaseActivity(), RatesMvpView {

    private val ratesRecyclerView by bindView<RecyclerView>(R.id.ratesRecyclerView)
    private val progressBar by bindView<View>(R.id.progressBar)
    private val errorView by bindView<View>(R.id.errorView)
    private val retryButton by bindView<View>(R.id.retryButton)

    @Inject
    lateinit var presenter: RatesPresenter

    @Inject
    lateinit var adapter: RatesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rates)
        activityComponent?.inject(this)
        presenter.bind(this)
        (ratesRecyclerView.itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = false
        ratesRecyclerView.layoutManager = LinearLayoutManager(this)
        ratesRecyclerView.adapter = adapter
        presenter.startObserveRates()
        ratesRecyclerView.setHasFixedSize(true)
        adapter.onAmountChanged { presenter.onAmountChanged(it) }
        adapter.onRateItemClick { currencyCode, amount ->
            presenter.onRateItemClick(currencyCode, amount)
        }
        retryButton.setOnClickListener {
            presenter.startObserveRates()
        }
    }

    override fun onDestroy() {
        presenter.destroy()
        super.onDestroy()
    }

    override fun showRatesList(list: List<RateItem>, needScrollToTop: Boolean) {
        ratesRecyclerView.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
        errorView.visibility = View.GONE
        adapter.submitList(list) {
            if (needScrollToTop) {
                ratesRecyclerView.scrollToPosition(0)
            }
        }
    }

    override fun showLoading() {
        ratesRecyclerView.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
        errorView.visibility = View.GONE
    }

    override fun showError() {
        ratesRecyclerView.visibility = View.GONE
        progressBar.visibility = View.GONE
        errorView.visibility = View.VISIBLE
    }
}
