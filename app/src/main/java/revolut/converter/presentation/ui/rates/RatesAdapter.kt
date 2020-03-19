package revolut.converter.presentation.ui.rates

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_rate, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
            holder.bind(getItem(position))

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val currencyFlag by bindView<ImageView>(R.id.currencyFlag)
        private val currencyCode by bindView<TextView>(R.id.currencyCode)
        private val currencyName by bindView<TextView>(R.id.currencyName)
        private val currencyAmount by bindView<EditText>(R.id.currencyAmount)

        fun bind(rateItem: RateItem) {
            currencyCode.text = rateItem.currencyCode
            currencyAmount.setText(rateItem.amount.toString())
        }
    }
}

class RatesItemDiffCallback @Inject constructor() : DiffUtil.ItemCallback<RateItem>() {
    override fun areItemsTheSame(oldItem: RateItem, newItem: RateItem): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: RateItem, newItem: RateItem): Boolean {
        return oldItem == newItem
    }
}
