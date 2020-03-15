package revolut.converter.di.module

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import revolut.converter.data.model.Rate
import revolut.converter.data.remote.RatesApi
import revolut.converter.data.remote.deserializer.RateDeserializer
import javax.inject.Singleton

@Module
open class NetworkModule {

    companion object {
        const val API_URL = "https://hiring.revolut.codes/api/android/"
    }

    @Provides
    @Singleton
    fun provideRateDeserializer(): RateDeserializer = RateDeserializer()

    @Provides
    @Singleton
    fun provideGson(rateDeserializer: RateDeserializer): Gson = GsonBuilder()
            .registerTypeAdapter(Rate::class.java, rateDeserializer)
            .create()

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder().build()

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(API_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()

    @Provides
    @Singleton
    open fun provideApiService(retrofit: Retrofit): RatesApi = retrofit.create(RatesApi::class.java)
}