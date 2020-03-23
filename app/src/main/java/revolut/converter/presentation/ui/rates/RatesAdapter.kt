package revolut.converter.presentation.ui.rates

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import revolut.converter.R
import revolut.converter.presentation.model.RateItem
import revolut.converter.util.bindView
import javax.inject.Inject

class RatesAdapter @Inject constructor(ratesItemDiffCallback: RatesItemDiffCallback) :
    ListAdapter<RateItem, RatesAdapter.ViewHolder>(ratesItemDiffCallback) {

    private var amountChangedCallback: (String) -> Unit = {}
    private var rateClickCallback: (String, String) -> Unit = { _, _ -> }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_rate, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getItem(position))

    fun onAmountChanged(callback: (String) -> Unit) {
        amountChangedCallback = callback
    }

    fun onRateItemClick(callback: (String, String) -> Unit) {
        rateClickCallback = callback
    }

    private var baseCursorPosition = 0

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val rateItemLayout by bindView<View>(R.id.rateItemLayout)
        private val currencyFlag by bindView<ImageView>(R.id.currencyFlag)
        private val currencyCode by bindView<TextView>(R.id.currencyCode)
        private val currencyName by bindView<TextView>(R.id.currencyName)

        private val currencyAmount by bindView<EditText>(R.id.currencyAmount)
        private val amountTextWatcher: TextWatcher = object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                if (!s.isNullOrEmpty()) {
                    baseCursorPosition = if (after == 1) start + 1 else start
                }
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) {
                    amountChangedCallback("0")
                } else {
                    amountChangedCallback(s.toString())
                }
            }

        }

        fun bind(rateItem: RateItem) {
            if (adapterPosition == 0) {
                currencyAmount.addTextChangedListener(amountTextWatcher)
            } else {
                currencyAmount.removeTextChangedListener(amountTextWatcher)
            }
            currencyAmount.setText(rateItem.amount)
            if (adapterPosition == 0) {
                val length = rateItem.amount.length
                if (baseCursorPosition > length) baseCursorPosition = length
                currencyAmount.setSelection(baseCursorPosition)
            }
            currencyCode.text = rateItem.currencyCode
            currencyName.text = rateItem.currencyName
            rateItemLayout.setOnClickListener {
                rateClickCallback(rateItem.currencyCode, rateItem.amount)
                currencyAmount.setSelection(rateItem.amount.length)
            }
        }
    }
}

class RatesItemDiffCallback @Inject constructor() : DiffUtil.ItemCallback<RateItem>() {
    override fun areItemsTheSame(oldItem: RateItem, newItem: RateItem): Boolean {
        return oldItem.currencyCode == newItem.currencyCode
    }

    override fun areContentsTheSame(oldItem: RateItem, newItem: RateItem): Boolean {
        return oldItem == newItem
    }
}
