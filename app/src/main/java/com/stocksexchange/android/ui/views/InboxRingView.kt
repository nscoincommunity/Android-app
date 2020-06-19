package com.stocksexchange.android.ui.views

import android.content.Context
import android.util.AttributeSet
import com.stocksexchange.android.R
import com.stocksexchange.android.utils.extensions.getSelectableItemBackgroundBorderlessDrawable
import com.stocksexchange.android.ui.views.base.containers.BaseFrameLayoutView
import com.stocksexchange.core.utils.extensions.makeGone
import com.stocksexchange.core.utils.extensions.makeVisible
import kotlinx.android.synthetic.main.inbox_ring_view.view.*

/**
 * A view container showing an notification ring item count
 */
class InboxRingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseFrameLayoutView(context, attrs, defStyleAttr) {



    override fun init() {
        super.init()

        layoutParams = LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        )

        background = context.getSelectableItemBackgroundBorderlessDrawable()
    }


    fun setInboxCountMessage(count: Int) {
        when {
            count <= 0 -> inboxCountRL.makeGone()
            count in 1..9 -> {
                inboxCountTV.text = count.toString()
                inboxCountRL.makeVisible()
            }
            else -> {
                inboxCountTV.text = "9+"
                inboxCountRL.makeVisible()
            }
        }
    }


    override fun getLayoutResourceId(): Int = R.layout.inbox_ring_view


}