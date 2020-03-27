package revolut.converter.presentation.ui.rates.viewholder

import android.text.Editable
import android.text.InputFilter
import android.text.Spannable
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
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
import revolut.converter.presentation.util.RegexMaskTextWatcher
import revolut.converter.presentation.util.bindView
import java.util.regex.Matcher
import java.util.regex.Pattern

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

    private val stylingTextWatcher: TextWatcher = object : TextWatcher {
        private val grayColor = ContextCompat.getColor(itemView.context, R.color.gray)
        private val blackColor = ContextCompat.getColor(itemView.context, R.color.black)

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
            val textColor = if (!s.isNullOrEmpty() && s.toString().toDoubleOrNull() == 0.0) {
                grayColor
            } else {
                blackColor
            }
            s?.setSpan(
                ForegroundColorSpan(textColor),
                0,
                s.length,
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE
            )
        }
    }

    init {
        currencyAmount.addTextChangedListener(stylingTextWatcher)
    }

    fun bind(rateItem: RateItem) {
        doViewTypeSpecificLogic(rateItem)
        currencyAmount.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                rateClickCallback(rateItem.currencyCode, rateItem.amount)
            }
        }
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