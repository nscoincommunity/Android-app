package com.stocksexchange.android.ui.transactions.fragment

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.text.toSpannable
import androidx.recyclerview.widget.RecyclerView
import com.arthurivanets.adapster.Adapter
import com.arthurivanets.adapster.markers.ItemResources
import com.arthurivanets.adapster.model.BaseItem
import com.arthurivanets.adapster.model.Item
import com.arthurivanets.adapster.model.markers.Trackable
import com.stocksexchange.android.R
import com.stocksexchange.api.model.rest.Transaction
import com.stocksexchange.api.model.rest.TransactionType
import com.stocksexchange.android.ui.base.viewholders.BaseViewHolder
import com.stocksexchange.android.theming.ThemingUtil
import com.stocksexchange.android.theming.model.Theme
import com.stocksexchange.android.ui.views.mapviews.DottedMapView
import com.stocksexchange.core.utils.extensions.*
import com.stocksexchange.core.utils.extensions.makeGone
import com.stocksexchange.core.utils.text.CustomLinkMovementMethod
import com.stocksexchange.core.utils.text.SelectorSpan

class TransactionItem(itemModel: Transaction) : BaseItem<
    Transaction,
    TransactionItem.ViewHolder,
    TransactionResources
>(itemModel), Trackable<Long> {


    companion object {

        const val MAIN_LAYOUT_ID = R.layout.transaction_item_layout

    }




    override fun init(adapter: Adapter<out Item<RecyclerView.ViewHolder, ItemResources>>?, parent: ViewGroup,
                      inflater: LayoutInflater, resources: TransactionResources?): ViewHolder {
        return ViewHolder(inflater.inflate(layout, parent, false)).apply {
            val strings = resources!!.strings
            val theme = resources.theme

            mAmountDmv.setTitleText(strings[TransactionResources.STRING_AMOUNT])
            mFeeDmv.setTitleText(strings[TransactionResources.STRING_FEE])
            mConfirmationsDmv.setTitleText(strings[TransactionResources.STRING_CONFIRMATIONS])
            mAddressDmv.setTitleText(strings[TransactionResources.STRING_ADDRESS])
            mTransactionExplorerIdDmv.setTitleText(strings[TransactionResources.STRING_TRANSACTION_ID])
            mDateDmv.setTitleText(strings[TransactionResources.STRING_DATE])

            applyTheme(theme)
        }
    }


    override fun bind(adapter: Adapter<out Item<RecyclerView.ViewHolder, ItemResources>>?,
                      viewHolder: ViewHolder, resources: TransactionResources?) {
        super.bind(adapter, viewHolder, resources)

        with(viewHolder) {
            val strings = resources!!.strings
            val colors = resources.colors
            val numberFormatter = resources.numberFormatter
            val timeFormatter = resources.timeFormatter
            val feeCurrencySymbol = itemModel.feeCurrencySymbol

            mStatusTv.text = itemModel.status
            mCurrencyTv.text = itemModel.currencySymbol

            mAmountDmv.setValueText(numberFormatter.formatAmount(itemModel.amount))
            mFeeDmv.setValueText("${numberFormatter.formatTransactionFee(itemModel.fee)} $feeCurrencySymbol")
            mDateDmv.setValueText(timeFormatter.formatDate(itemModel.timestampInMillis))

            handleConfirmationsView(mConfirmationsDmv, resources)

            if((itemModel.type == TransactionType.WITHDRAWALS) && itemModel.withdrawal.isNotConfirmed) {
                mLeftActionBtnTv.text = strings[TransactionResources.STRING_RESEND_CONFIRMATION_EMAIL]
                mRightActionBtnTv.text = strings[TransactionResources.STRING_CANCEL]

                mButtonsContainerLl.makeVisible()
            } else {
                mButtonsContainerLl.makeGone()
            }

            ThemingUtil.Transactions.TransactionItem.statusButton(
                textView = mStatusTv,
                theme = resources.theme,
                backgroundColor = if(itemModel.statusColor.isColor()) {
                    Color.parseColor(itemModel.statusColor)
                } else {
                    colors[TransactionResources.COLOR_BLUE_ACCENT]
                }
            )
        }
    }


    private fun handleConfirmationsView(confirmationsDmv: DottedMapView,
                                        resources: TransactionResources) {
        if(itemModel.type == TransactionType.DEPOSITS) {
            val confirmations = itemModel.deposit.confirmations

            if(confirmations == null) {
                confirmationsDmv.makeGone()
            } else {
                val received = confirmations.receivedConfirmationsNumber
                val total = confirmations.totalConfirmationsNumber
                val preposition = resources.strings[TransactionResources.STRING_PREPOSITION_OF]
                val value = "$received $preposition $total"

                confirmationsDmv.setValueText(value)
                confirmationsDmv.makeVisible()
            }
        } else {
            confirmationsDmv.makeGone()
        }
    }


    fun setOnTransactionAddressClickListener(viewHolder: ViewHolder, resources: TransactionResources?,
                                             position: Int, listener: ((View, TransactionItem, Int) -> Unit)?) {
        with(viewHolder) {
            if(itemModel.type == TransactionType.WITHDRAWALS) {
                val addressValue = itemModel.withdrawal.addressData.addressParameterValue
                val spannableString = addressValue.truncate(15).toSpannable().apply {
                    this[0, length] = getClickableSpanForEntry(resources, position, listener)
                }

                mAddressDmv.setValueMovementMethod(CustomLinkMovementMethod())
                mAddressDmv.setValueText(spannableString)
                mAddressDmv.makeVisible()
            } else {
                mAddressDmv.makeGone()
            }
        }
    }


    fun setOnTransactionExplorerIdClickListener(viewHolder: ViewHolder, resources: TransactionResources?,
                                                position: Int, listener: ((View, TransactionItem, Int) -> Unit)?) {
        with(viewHolder) {
            if(itemModel.hasTransactionExplorerId) {
                if(itemModel.isInternal) {
                    mTransactionExplorerIdDmv.setValueText(resources!!.strings[
                        TransactionResources.STRING_INTERNAL_TRANSFER
                    ])
                } else {
                    val spannableString = itemModel.transactionExplorerId.truncate(15).toSpannable().apply {
                        this[0, length] = getClickableSpanForEntry(resources, position, listener)
                    }

                    mTransactionExplorerIdDmv.setValueMovementMethod(CustomLinkMovementMethod())
                    mTransactionExplorerIdDmv.setValueText(spannableString)
                }

                mTransactionExplorerIdDmv.makeVisible()
            } else {
                mTransactionExplorerIdDmv.makeGone()
            }
        }
    }


    fun setOnLeftActionButtonClickListener(viewHolder: ViewHolder, position: Int,
                                           listener: ((View, TransactionItem, Int) -> Unit)?) {
        viewHolder.mLeftActionBtnTv.setOnClickListener {
            listener?.invoke(it, this@TransactionItem, position)
        }
    }


    fun setOnRightActionButtonClickListener(viewHolder: ViewHolder, position: Int,
                                            listener: ((View, TransactionItem, Int) -> Unit)?) {
        viewHolder.mRightActionBtnTv.setOnClickListener {
            listener?.invoke(it, this@TransactionItem, position)
        }
    }


    private fun getClickableSpanForEntry(resources: TransactionResources?, position: Int,
                                         listener: ((View, TransactionItem, Int) -> Unit)?): SelectorSpan {
        return object : SelectorSpan(
            resources!!.theme.generalTheme.linkReleasedStateBackgroundColor,
            resources.theme.generalTheme.linkPressedStateBackgroundColor
        ) {

            override fun onClick(widget: View) {
                listener?.invoke(widget, this@TransactionItem, position)
            }

        }
    }


    override fun getLayout(): Int = MAIN_LAYOUT_ID


    override fun getTrackKey(): Long = itemModel.id


    class ViewHolder(itemView: View) : BaseViewHolder<Transaction>(itemView) {

        val mStatusTv: TextView = itemView.findViewById(R.id.statusTv)
        val mCurrencyTv: TextView = itemView.findViewById(R.id.currencyTv)
        val mLeftActionBtnTv: TextView = itemView.findViewById(R.id.leftActionBtnTv)
        val mRightActionBtnTv: TextView = itemView.findViewById(R.id.rightActionBtnTv)

        val mAmountDmv: DottedMapView = itemView.findViewById(R.id.amountDmv)
        val mFeeDmv: DottedMapView = itemView.findViewById(R.id.feeDmv)
        val mConfirmationsDmv: DottedMapView = itemView.findViewById(R.id.confirmationsDmv)
        val mAddressDmv: DottedMapView = itemView.findViewById(R.id.addressDmv)
        val mTransactionExplorerIdDmv: DottedMapView = itemView.findViewById(R.id.transactionExplorerIdDmv)
        val mDateDmv: DottedMapView = itemView.findViewById(R.id.dateDmv)

        val mButtonsContainerLl: LinearLayout = itemView.findViewById(R.id.buttonsContainerLl)

        val mCardView: CardView = itemView.findViewById(R.id.cardView)


        override fun applyTheme(theme: Theme) {
            with(ThemingUtil.Transactions.TransactionItem) {
                cardView(mCardView, theme)
                title(mCurrencyTv, theme)

                dottedMap(mAmountDmv, theme)
                dottedMap(mFeeDmv, theme)
                dottedMap(mConfirmationsDmv, theme)
                dottedMap(mAddressDmv, theme)
                dottedMap(mTransactionExplorerIdDmv, theme)
                dottedMap(mDateDmv, theme)

                leftActionButton(mLeftActionBtnTv, theme)
                rightActionButton(mRightActionBtnTv, theme)
            }
        }

    }


}