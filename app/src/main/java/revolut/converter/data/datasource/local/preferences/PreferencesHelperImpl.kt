package revolut.converter.data.datasource.local.preferences

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesHelperImpl @Inject constructor(val prefs: SharedPreferences) : PreferencesHelper {
    companion object {
        private val KEY_CURRENCY_CODE = "currency_code"
        private val KEY_AMOUNT = "amount"
        const val DEFAULT_CURRENCY = "EUR"
        const val DEFAULT_AMOUNT = 100.0
    }

    override var currencyCode: String
        get() = prefs.getString(KEY_CURRENCY_CODE, DEFAULT_CURRENCY) ?: DEFAULT_CURRENCY
        set(value) = prefs.edit().putString(KEY_CURRENCY_CODE, value).apply()

    override var amount: Double
        get() = prefs.getDouble(KEY_AMOUNT, DEFAULT_AMOUNT)
        set(value) = prefs.edit().putDouble(KEY_AMOUNT, value).apply()
}

private fun SharedPreferences.Editor.putDouble(key: String, double: Double) =
    putLong(key, java.lang.Double.doubleToRawLongBits(double))

private fun SharedPreferences.getDouble(key: String, default: Double) =
    java.lang.Double.longBitsToDouble(getLong(key, java.lang.Double.doubleToRawLongBits(default)))