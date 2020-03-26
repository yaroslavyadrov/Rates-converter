package revolut.converter.presentation.ui.rates.viewholder

import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import revolut.converter.R
import revolut.converter.presentation.model.RateItem
import revolut.converter.presentation.util.bindView

abstract class RateViewHolder(
    itemView: View,
    private val rateClickCallback: (String, String) -> Unit
) : RecyclerView.ViewHolder(itemView) {

    protected val rateItemLayout by bindView<View>(R.id.rateItemLayout)
    protected val currencyFlag by bindView<ImageView>(R.id.currencyFlag)
    protected val currencyCode by bindView<TextView>(R.id.currencyCode)
    protected val currencyName by bindView<TextView>(R.id.currencyName)
    protected val currencyAmount by bindView<EditText>(R.id.currencyAmount)

    protected abstract fun doViewTypeSpecificLogic(rateItem: RateItem)

    fun bind(rateItem: RateItem) {
        doViewTypeSpecificLogic(rateItem)
        currencyAmount.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                rateClickCallback(rateItem.currencyCode, rateItem.amount)
            }
        }
        currencyAmount.setTextColor(
            ContextCompat.getColor(
                itemView.context,
                rateItem.textColor
            )
        )
        currencyCode.text = rateItem.currencyCode
        currencyName.text = rateItem.currencyName
        rateItemLayout.setOnClickListener {
            rateClickCallback(rateItem.currencyCode, rateItem.amount)
        }
        Glide.with(itemView)
            .load(rateItem.flagId)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .apply(RequestOptions.circleCropTransform())
            .into(currencyFlag)
    }
}