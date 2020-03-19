package revolut.converter.di.component

import dagger.Component
import revolut.converter.di.module.LocalDataModule
import revolut.converter.di.module.NetworkModule
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, LocalDataModule::class])
interface AppComponent {

    fun activityComponent(): ActivityComponent
}