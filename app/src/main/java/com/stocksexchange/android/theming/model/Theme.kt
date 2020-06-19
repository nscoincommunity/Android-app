package com.stocksexchange.android.theming.model

import java.io.Serializable

data class Theme(
    val id: Int,
    val name: String,
    val generalTheme: GeneralTheme,
    val buttonTheme: ButtonTheme,
    val cardViewTheme: CardViewTheme,
    val switchOptionsViewTheme: SwitchOptionsViewTheme,
    val priceChartViewTheme: PriceChartViewTheme,
    val depthChartViewTheme: DepthChartViewTheme,
    val tradeHistoryViewTheme: TradeHistoryViewTheme,
    val orderbookViewTheme: OrderbookViewTheme,
    val dialogTheme: DialogTheme,
    val dottedMapViewTheme: DottedMapViewTheme,
    val sortPanelTheme: SortPanelTheme,
    val switchTheme: SwitchTheme,
    val popupMenuTheme: PopupMenuTheme,
    val pinEntryKeypadTheme: PinEntryKeypadTheme,
    val aboutTheme: AboutTheme
) : Serializable {


    companion object {

        val STUB = Theme(
            id = -1,
            name = "",
            generalTheme = GeneralTheme.STUB,
            buttonTheme = ButtonTheme.STUB,
            cardViewTheme = CardViewTheme.STUB,
            switchOptionsViewTheme = SwitchOptionsViewTheme.STUB,
            priceChartViewTheme = PriceChartViewTheme.STUB,
            depthChartViewTheme = DepthChartViewTheme.STUB,
            tradeHistoryViewTheme = TradeHistoryViewTheme.STUB,
            orderbookViewTheme = OrderbookViewTheme.STUB,
            dialogTheme = DialogTheme.STUB,
            dottedMapViewTheme = DottedMapViewTheme.STUB,
            sortPanelTheme = SortPanelTheme.STUB,
            switchTheme = SwitchTheme.STUB,
            popupMenuTheme = PopupMenuTheme.STUB,
            pinEntryKeypadTheme = PinEntryKeypadTheme.STUB,
            aboutTheme = AboutTheme.STUB
        )

    }


}