package com.stocksexchange.android.ui.wallets.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.arthurivanets.adapster.Adapter
import com.arthurivanets.adapster.markers.ItemResources
import com.arthurivanets.adapster.model.BaseItem
import com.arthurivanets.adapster.model.Item
import com.arthurivanets.adapster.model.markers.Trackable
import com.stocksexchange.android.R
import com.stocksexchange.api.model.rest.Wallet
import com.stocksexchange.android.ui.base.viewholders.BaseViewHolder
import com.stocksexchange.android.theming.ThemingUtil
import com.stocksexchange.android.theming.model.Theme
import com.stocksexchange.android.ui.views.mapviews.DottedMapView
import com.stocksexchange.api.model.CoinStatus
import com.stocksexchange.core.utils.extensions.*
import com.stocksexchange.core.utils.extensions.makeGone

class WalletItem(itemModel: Wallet) : BaseItem<
    Wallet,
    WalletItem.ViewHolder,
    WalletsResources
>(itemModel), Trackable<Int> {


    companion object {

        const val MAIN_LAYOUT_ID = R.layout.wallet_item_layout

    }




    override fun init(adapter: Adapter<out Item<RecyclerView.ViewHolder, ItemResources>>?, parent: ViewGroup,
                      inflater: LayoutInflater, resources: WalletsResources?): ViewHolder {
        return ViewHolder(inflater.inflate(layout, parent, false)).apply {
            val strings = resources!!.strings
            val theme = resources.settings.theme

            mCoinStatusDmv.setTitleText("${strings[WalletsResources.STRING_COIN_STATUS]}:")
            mAvailableBalanceDmv.setTitleText("${strings[WalletsResources.STRING_AVAILABLE_BALANCE]}:")
            mBalanceInOrdersDmv.setTitleText("${strings[WalletsResources.STRING_BALANCE_IN_ORDERS]}:")
            mBonusBalanceDmv.setTitleText("${strings[WalletsResources.STRING_BONUS_BALANCE]}:")
            mTotalBalanceDmv.setTitleText("${strings[WalletsResources.STRING_TOTAL_BALANCE]}:")

            mDepositTvBtn.text = strings[WalletsResources.STRING_ACTION_DEPOSIT]
            mWithdrawTvBtn.text = strings[WalletsResources.STRING_ACTION_WITHDRAW]

            applyTheme(theme)
        }
    }


    override fun bind(adapter: Adapter<out Item<RecyclerView.ViewHolder, ItemResources>>?,
                      viewHolder: ViewHolder, resources: WalletsResources?) {
        super.bind(adapter, viewHolder, resources)

        with(viewHolder) {
            val stringProvider = resources!!.stringProvider
            val numberFormatter = resources.numberFormatter

            mCurrencyNameTv.text = itemModel.currencySymbol
            mCurrencyLongNameTv.text = itemModel.currencyName

            mCoinStatusDmv.setValueText(stringProvider.getString(itemModel.status.stringId))
            mCoinStatusDmv.setValueColor(mCoinStatusDmv.getColor(itemModel.status.colorId))

            mAvailableBalanceDmv.setValueText(numberFormatter.formatBalance(itemModel.currentBalance))
            mBalanceInOrdersDmv.setValueText(numberFormatter.formatBalance(itemModel.frozenBalance))
            mBonusBalanceDmv.setValueText(numberFormatter.formatBalance(itemModel.bonusBalance))
            mTotalBalanceDmv.setValueText(numberFormatter.formatBalance(itemModel.totalBalance))

            if(itemModel.isDepositingEnabled) {
                mDepositTvBtn.makeVisible()
            } else {
                mDepositTvBtn.makeGone()
            }

            when(itemModel.status) {
                CoinStatus.DISABLED -> mButtonsContainerLl.makeGone()

                else -> mButtonsContainerLl.makeVisible()
            }
        }
    }


    fun setOnCurrencyNameClickListener(viewHolder: ViewHolder, position: Int,
                                       listener: ((View, WalletItem, Int) -> Unit)?) {
        viewHolder.mCurrencyNameTv.setOnClickListener {
            listener?.invoke(it, this@WalletItem, position)
        }
    }


    fun setOnDepositButtonClickListener(viewHolder: ViewHolder, position: Int,
                                        listener: ((View, WalletItem, Int) -> Unit)?) {
        viewHolder.mDepositTvBtn.setOnClickListener {
            listener?.invoke(it, this@WalletItem, position)
        }
    }


    fun setOnWithdrawButtonClickListener(viewHolder: ViewHolder, position: Int,
                                         listener: ((View, WalletItem, Int) -> Unit)?) {
        viewHolder.mWithdrawTvBtn.setOnClickListener {
            listener?.invoke(it, this@WalletItem, position)
        }
    }


    override fun getLayout(): Int = MAIN_LAYOUT_ID


    override fun getTrackKey(): Int = itemModel.currencyId


    class ViewHolder(itemView: View) : BaseViewHolder<Wallet>(itemView) {

        val mCurrencyNameTv: TextView = itemView.findViewById(R.id.currencyNameTv)
        val mCurrencyLongNameTv: TextView = itemView.findViewById(R.id.currencyLongNameTv)

        val mCoinStatusDmv: DottedMapView = itemView.findViewById(R.id.coinStatusDmv)
        val mAvailableBalanceDmv: DottedMapView = itemView.findViewById(R.id.availableBalanceDmv)
        val mBalanceInOrdersDmv: DottedMapView = itemView.findViewById(R.id.balanceInOrdersDmv)
        val mBonusBalanceDmv: DottedMapView = itemView.findViewById(R.id.bonusBalanceDmv)
        val mTotalBalanceDmv: DottedMapView = itemView.findViewById(R.id.totalBalanceDmv)

        val mDepositTvBtn: TextView = itemView.findViewById(R.id.depositTvBtn)
        val mWithdrawTvBtn: TextView = itemView.findViewById(R.id.withdrawTvBtn)

        val mButtonsContainerLl: LinearLayout = itemView.findViewById(R.id.buttonsContainerLl)

        val mCardView: CardView = itemView.findViewById(R.id.cardView)


        override fun applyTheme(theme: Theme) {
            with(ThemingUtil.Wallets.WalletItem) {
                cardView(mCardView, theme)
                currencyName(mCurrencyNameTv, theme)
                currencyLongName(mCurrencyLongNameTv, theme)
                dottedMap(mCoinStatusDmv, theme)
                dottedMap(mAvailableBalanceDmv, theme)
                dottedMap(mBalanceInOrdersDmv, theme)
                dottedMap(mBonusBalanceDmv, theme)
                dottedMap(mTotalBalanceDmv, theme)
                depositButton(mDepositTvBtn, theme)
                withdrawButton(mWithdrawTvBtn, theme)
            }
        }

    }


}