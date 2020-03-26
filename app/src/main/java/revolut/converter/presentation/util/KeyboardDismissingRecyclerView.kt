package revolut.converter.presentation.util

import android.content.Context
import android.util.AttributeSet
import android.view.inputmethod.InputMethodManager
import androidx.annotation.Nullable
import androidx.recyclerview.widget.RecyclerView

class KeyboardDismissingRecyclerView @JvmOverloads constructor(
    context: Context,
    @Nullable attrs: AttributeSet? = null,
    defStyle: Int = -1
) : RecyclerView(context, attrs, defStyle) {

    private var keyboardDismissingScrollListener: OnScrollListener = object : OnScrollListener() {
        var isKeyboardDismissedByScroll = false
        override fun onScrollStateChanged(recyclerView: RecyclerView, state: Int) {
            when (state) {
                SCROLL_STATE_DRAGGING -> if (!isKeyboardDismissedByScroll) {
                    hideKeyboard()
                    isKeyboardDismissedByScroll = !isKeyboardDismissedByScroll
                }
                SCROLL_STATE_IDLE -> isKeyboardDismissedByScroll = false
            }
        }
    }

    private val inputMethodManager: InputMethodManager by lazy {
        context.getSystemService(
            Context.INPUT_METHOD_SERVICE
        ) as InputMethodManager
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        addOnScrollListener(keyboardDismissingScrollListener)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        removeOnScrollListener(keyboardDismissingScrollListener)
    }

    fun hideKeyboard() {
        inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
        clearFocus()
    }
}