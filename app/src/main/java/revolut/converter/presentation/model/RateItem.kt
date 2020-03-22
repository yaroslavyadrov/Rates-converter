package revolut.converter.presentation.model

import androidx.annotation.DrawableRes

data class RateItem(
        @DrawableRes val flagId: Int,
        val currencyCode: String,
        val currencyName: String,
        val amount: String
)