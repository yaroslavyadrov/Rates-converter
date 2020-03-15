package revolut.converter.data.remote

import javax.inject.Inject

class RatesRepository @Inject constructor(private val api: RatesApi) {

    fun getLatestRates(currencyCode: String = "EUR") = api.getLatestRates(currencyCode)
}