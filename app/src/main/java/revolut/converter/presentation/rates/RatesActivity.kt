package revolut.converter.presentation.rates

import android.os.Bundle
import revolut.converter.R
import revolut.converter.presentation.base.BaseActivity
import javax.inject.Inject

class RatesActivity : BaseActivity(), RatesMvpView {

    @Inject
    lateinit var presenter: RatesPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        activityComponent?.inject(this)
        presenter.bind(this)
    }

    override fun onDestroy() {
        presenter.destroy()
        super.onDestroy()
    }
}
