package revolut.converter.data.datasource.remote

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query
import revolut.converter.data.datasource.remote.model.RatesList

interface RatesApi {
    @GET("latest")
    fun getLatestRates(@Query("base") baseCurrency: String): Single<RatesList>
}