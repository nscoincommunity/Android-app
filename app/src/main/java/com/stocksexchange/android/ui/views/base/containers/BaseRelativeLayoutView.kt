package com.stocksexchange.android.ui.views.base.containers

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import com.stocksexchange.android.model.Attributes
import com.stocksexchange.android.ui.views.base.containers.interfaces.BaseContainerView
import com.stocksexchange.android.utils.providers.StringProvider
import org.koin.core.inject

/**
 * A base view class to use if the custom view in question extends
 * a [RelativeLayout] class.
 */
@Suppress("LeakingThis")
abstract class BaseRelativeLayoutView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr), BaseContainerView {


    protected val mAttributes: Attributes = Attributes()

    protected val mStringProvider: StringProvider by inject()




    init {
        View.inflate(context, getLayoutResourceId(), this)
        saveAttributes(attrs, defStyleAttr)
    }


    override fun onFinishInflate() {
        super.onFinishInflate()

        init()
        applyAttributes()

        mAttributes.recycle()
    }


}