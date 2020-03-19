package revolut.converter.presentation.ui.rates

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import revolut.converter.R
import revolut.converter.presentation.model.RateItem
import revolut.converter.presentation.ui.base.BaseActivity
import revolut.converter.util.bindView
import javax.inject.Inject

class RatesActivity : BaseActivity(), RatesMvpView {

    private val ratesRecyclerView by bindView<RecyclerView>(R.id.ratesRecyclerView)

    @Inject
    lateinit var presenter: RatesPresenter

    @Inject
    lateinit var adapter: RatesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        activityComponent?.inject(this)
        presenter.bind(this)
        ratesRecyclerView.layoutManager = LinearLayoutManager(this)
        ratesRecyclerView.adapter = adapter
        presenter.onScreenOpen()
    }

    override fun onDestroy() {
        presenter.destroy()
        super.onDestroy()
    }

    override fun showRatesLst(list: List<RateItem>) {
        adapter.submitList(list)
    }
}
