package revolut.converter

import android.app.Application
import revolut.converter.di.component.AppComponent
import revolut.converter.di.component.DaggerAppComponent
import revolut.converter.di.module.NetworkModule

class RatesApp : Application() {
    lateinit var appComponent: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()
        initDagger()
    }

    private fun initDagger() {
        appComponent = DaggerAppComponent.builder()
            .networkModule(NetworkModule())
            .build()
    }

}