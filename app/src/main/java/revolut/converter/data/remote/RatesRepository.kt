package revolut.converter.data.remote

import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import revolut.converter.data.remote.model.RatesList
import javax.inject.Inject

class RatesRepository @Inject constructor(private val api: RatesApi) {

    fun getLatestRates(currencyCode: String = "EUR"): Single<RatesList> {
        return api.getLatestRates(currencyCode).subscribeOn(Schedulers.io())
    }
}