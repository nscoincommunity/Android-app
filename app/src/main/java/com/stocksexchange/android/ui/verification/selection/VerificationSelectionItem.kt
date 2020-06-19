package com.stocksexchange.android.ui.verification.selection

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.text.Spannable
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.text.toSpannable
import androidx.recyclerview.widget.RecyclerView
import com.arthurivanets.adapster.Adapter
import com.arthurivanets.adapster.markers.ItemResources
import com.arthurivanets.adapster.model.BaseItem
import com.arthurivanets.adapster.model.Item
import com.stocksexchange.android.R
import com.stocksexchange.api.model.rest.VerificationType
import com.stocksexchange.android.theming.ThemingUtil
import com.stocksexchange.android.theming.model.Theme
import com.stocksexchange.android.ui.base.viewholders.BaseViewHolder
import com.stocksexchange.android.utils.providers.StringProvider
import com.stocksexchange.core.utils.extensions.*

class VerificationSelectionItem(itemModel: VerificationType) : BaseItem<
    VerificationType,
    VerificationSelectionItem.ViewHolder,
    VerificationSelectionResources
>(itemModel) {


    companion object {

        const val MAIN_LAYOUT_ID = R.layout.verification_selection_item_layout

    }




    override fun init(adapter: Adapter<out Item<RecyclerView.ViewHolder, ItemResources>>?, parent: ViewGroup,
                      inflater: LayoutInflater, resources: VerificationSelectionResources?): ViewHolder {
        return ViewHolder(inflater.inflate(layout, parent, false)).apply {
            applyTheme(resources!!.settings.theme)
        }
    }


    @SuppressLint("DefaultLocale")
    override fun bind(adapter: Adapter<out Item<RecyclerView.ViewHolder, ItemResources>>?,
                      viewHolder: ViewHolder, resources: VerificationSelectionResources?) {
        super.bind(adapter, viewHolder, resources)

        with(viewHolder) {
            val profileInfo = resources!!.profileInfo
            val theme = resources.settings.theme
            val stringProvider = resources.stringProvider
            val numberFormatter = resources.numberFormatter
            val strings = resources.strings

            if(profileInfo.isVerifiedBy(itemModel)) {
                verifiedTv.text = strings[VerificationSelectionResources.STRING_VERIFIED]
                verifiedTv.makeVisible()
            } else {
                verifiedTv.makeGone()
            }

            val feePercent = profileInfo.getTradingFeeInPercentage(itemModel)

            if(feePercent > 0.0) {
                feeTv.text = numberFormatter.formatVerificationFee(feePercent)
                feeTv.makeVisible()
            } else {
                feeTv.makeGone()
            }

            with(iconIv) {
                setTopMargin(context.resources.getDimensionPixelSize(itemModel.iconTopMarginId))
                setBottomMargin(context.resources.getDimensionPixelSize(itemModel.iconBottomMarginId))
                setImageResource(itemModel.iconId)
            }

            titleTv.text = stringProvider.getString(itemModel.titleId)

            with(descriptionTv) {
                setLineCount(itemModel.descriptionLineCount)
                text = getSpannableDescription(stringProvider, theme)
            }
        }
    }


    private fun getSpannableDescription(stringProvider: StringProvider,
                                        theme: Theme): Spannable {
        val descriptionArgument = stringProvider.getString(itemModel.descriptionArgId)
        val description = stringProvider.getString(itemModel.descriptionId, descriptionArgument)

        return description.toSpannable().apply {
            val generalTheme = theme.generalTheme
            val accentColor = generalTheme.accentColor
            val secondaryTextColor = generalTheme.secondaryTextColor

            this[0, descriptionArgument.length] = ForegroundColorSpan(accentColor)
            this[0, descriptionArgument.length] = StyleSpan(Typeface.BOLD)
            this[descriptionArgument.length, description.length] = ForegroundColorSpan(secondaryTextColor)
        }
    }


    fun setOnItemClickListener(viewHolder: ViewHolder, position: Int,
                               listener: ((ViewHolder, VerificationSelectionItem, Int) -> Unit)?) {
        viewHolder.itemView.setOnClickListener {
            listener?.invoke(viewHolder, this@VerificationSelectionItem, position)
        }
    }


    override fun getLayout(): Int = MAIN_LAYOUT_ID


    class ViewHolder(itemView: View) : BaseViewHolder<VerificationType>(itemView) {

        val verifiedTv: TextView = itemView.findViewById(R.id.verifiedTv)
        val feeTv: TextView = itemView.findViewById(R.id.feeTv)

        val iconIv: ImageView = itemView.findViewById(R.id.iconIv)

        val titleTv: TextView = itemView.findViewById(R.id.titleTv)
        val descriptionTv: TextView = itemView.findViewById(R.id.descriptionTv)

        val cardView: CardView = itemView.findViewById(R.id.cardView)


        override fun applyTheme(theme: Theme) {
            with(ThemingUtil.VerificationSelection.Item) {
                cardView(cardView, theme)
                verifiedText(verifiedTv, theme)
                feeText(feeTv, theme)
                titleText(titleTv, theme)
            }
        }

    }


}