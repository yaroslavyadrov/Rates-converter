package revolut.converter.presentation.ui.rates.viewholder

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import revolut.converter.presentation.model.RateItem

class BaseRateViewHolder(
    itemView: View,
    rateClickCallback: (String, String) -> Unit,
    private val amountChangedCallback: (String) -> Unit
) : RateViewHolder(itemView, rateClickCallback) {

    private var baseCursorPosition = 0

    private val amountTextWatcher: TextWatcher = object : TextWatcher {

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            if (!s.isNullOrEmpty()) {
                if (count != after) {
                    baseCursorPosition = if (after > 0) {
                        start + after
                    } else {
                        if (s.length == 1) {
                            count
                        } else {
                            start
                        }
                    }
                }
            }
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
            if (s.isNullOrEmpty()) {
                amountChangedCallback("0")
            } else {
                if (s.length == 1 && !s[0].isDigit()) {
                    amountChangedCallback("0")
                } else {
                    amountChangedCallback(s.toString())
                }
            }
        }
    }

    init {
        currencyAmount.addTextChangedListener(amountTextWatcher)
    }

    override fun doViewTypeSpecificLogic(rateItem: RateItem) {
        currencyAmount.setText(rateItem.amount)
        val length = rateItem.amount.length
        if (baseCursorPosition > length) baseCursorPosition = length
        currencyAmount.setSelection(baseCursorPosition)
    }
}