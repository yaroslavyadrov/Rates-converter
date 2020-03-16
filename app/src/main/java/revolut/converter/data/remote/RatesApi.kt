package revolut.converter.data.remote

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query
import revolut.converter.data.remote.model.RatesList

interface RatesApi {
    @GET("latest")
    fun getLatestRates(@Query("base") baseCurrency: String): Single<RatesList>
}