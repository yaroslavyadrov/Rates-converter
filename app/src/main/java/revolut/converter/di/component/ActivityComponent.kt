package revolut.converter.di.component

import dagger.Subcomponent
import revolut.converter.presentation.ui.rates.RatesActivity

@Subcomponent
interface ActivityComponent {
    fun inject(activity: RatesActivity)
}