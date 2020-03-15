package revolut.converter.presentation.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import revolut.converter.RatesApp
import revolut.converter.di.component.ActivityComponent


abstract class BaseActivity : AppCompatActivity() {

    var activityComponent: ActivityComponent? = null
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityComponent = (applicationContext as RatesApp).appComponent.activityComponent()
    }

    override fun onDestroy() {
        activityComponent = null
        super.onDestroy()
    }

}
