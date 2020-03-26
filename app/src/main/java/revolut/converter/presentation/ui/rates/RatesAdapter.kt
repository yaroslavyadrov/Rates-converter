package revolut.converter.presentation.ui.rates

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import revolut.converter.R
import revolut.converter.presentation.model.RateItem
import revolut.converter.presentation.ui.rates.viewholder.BaseRateViewHolder
import revolut.converter.presentation.ui.rates.viewholder.RateViewHolder
import revolut.converter.presentation.ui.rates.viewholder.RegularRateViewHolder
import javax.inject.Inject

class RatesAdapter @Inject constructor(ratesItemDiffCallback: RatesItemDiffCallback) :
        ListAdapter<RateItem, RateViewHolder>(ratesItemDiffCallback) {

    private val VIEW_TYPE_BASE_CURRENCY = 0
    private val VIEW_TYPE_REGULAR_CURRENCY = 1

    private var amountChangedCallback: (String) -> Unit = {}
    private var rateClickCallback: (String, String) -> Unit = { _, _ -> }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RateViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_rate, parent, false)
        return when (viewType) {
            VIEW_TYPE_BASE_CURRENCY -> BaseRateViewHolder(view, rateClickCallback, amountChangedCallback)
            VIEW_TYPE_REGULAR_CURRENCY -> RegularRateViewHolder(view, rateClickCallback)
            else -> throw RuntimeException("Wrong RecyclerView viewType")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) VIEW_TYPE_BASE_CURRENCY else VIEW_TYPE_REGULAR_CURRENCY
    }

    override fun onBindViewHolder(holder: RateViewHolder, position: Int) =
            holder.bind(getItem(position))

    fun onAmountChanged(callback: (String) -> Unit) {
        amountChangedCallback = callback
    }

    fun onRateItemClick(callback: (String, String) -> Unit) {
        rateClickCallback = callback
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
