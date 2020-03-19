package revolut.converter.data.datasource.local.preferences

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesHelperImpl @Inject constructor(val prefs: SharedPreferences) : PreferencesHelper {
    companion object {
        private val KEY_CURRENCY_CODE = "currency_code"
        private val KEY_AMOUNT = "amount"
    }

    override var currencyCode: String
        get() = prefs.getString(KEY_CURRENCY_CODE, "") ?: ""
        set(value) = prefs.edit().putString(KEY_CURRENCY_CODE, value).apply()

    override var amount: Float
        get() = prefs.getFloat(KEY_CURRENCY_CODE, 100f)
        set(value) = prefs.edit().putFloat(KEY_AMOUNT, value).apply()
}