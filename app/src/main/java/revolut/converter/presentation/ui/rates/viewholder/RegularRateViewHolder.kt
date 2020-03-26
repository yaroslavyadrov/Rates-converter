package revolut.converter.presentation.ui.rates.viewholder

import android.text.InputType
import android.view.View
import revolut.converter.presentation.model.RateItem

class RegularRateViewHolder(
    itemView: View,
    rateClickCallback: (String, String) -> Unit
) : RateViewHolder(itemView, rateClickCallback) {

    override fun doViewTypeSpecificLogic(rateItem: RateItem) {
        currencyAmount.inputType = InputType.TYPE_NULL
        currencyAmount.setText(rateItem.amount)
    }
}