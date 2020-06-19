package com.stocksexchange.android.theming.factories

import com.stocksexchange.android.theming.factories.base.BaseThemeFactory
import com.stocksexchange.android.theming.model.Theme
import com.stocksexchange.android.theming.model.Themes
import com.stocksexchange.core.providers.ColorProvider

class ThemeFactory(
    private val generalThemeFactory: GeneralThemeFactory,
    private val buttonThemeFactory: ButtonThemeFactory,
    private val cardViewThemeFactory: CardViewThemeFactory,
    private val switchOptionsViewThemeFactory: SwitchOptionsViewThemeFactory,
    private val priceChartViewThemeFactory: PriceChartViewThemeFactory,
    private val depthChartViewThemeFactory: DepthChartViewThemeFactory,
    private val orderbookViewThemeFactory: OrderbookViewThemeFactory,
    private val tradeHistoryViewThemeFactory: TradeHistoryViewThemeFactory,
    private val dialogThemeFactory: DialogThemeFactory,
    private val dottedMapViewThemeFactory: DottedMapViewThemeFactory,
    private val sortPanelThemeFactory: SortPanelThemeFactory,
    private val switchThemeFactory: SwitchThemeFactory,
    private val popupMenuThemeFactory: PopupMenuThemeFactory,
    private val pinEntryKeypadThemeFactory: PinEntryKeypadThemeFactory,
    private val aboutThemeFactory: AboutThemeFactory,
    colorProvider: ColorProvider
) : BaseThemeFactory<Theme>(colorProvider) {


    fun getDefaultTheme(): Theme {
        return getDeepTealTheme()
    }


    fun getThemeForId(id: Int): Theme {
        return when(id) {
            Themes.DEEP_TEAL.id -> getDeepTealTheme()

            else -> getDeepTealTheme()
        }
    }


    override fun getDeepTealTheme(): Theme {
        return Theme(
            id = Themes.DEEP_TEAL.id,
            name = Themes.DEEP_TEAL.title,
            generalTheme = generalThemeFactory.getDeepTealTheme(),
            buttonTheme = buttonThemeFactory.getDeepTealTheme(),
            cardViewTheme = cardViewThemeFactory.getDeepTealTheme(),
            switchOptionsViewTheme = switchOptionsViewThemeFactory.getDeepTealTheme(),
            priceChartViewTheme = priceChartViewThemeFactory.getDeepTealTheme(),
            depthChartViewTheme = depthChartViewThemeFactory.getDeepTealTheme(),
            orderbookViewTheme = orderbookViewThemeFactory.getDeepTealTheme(),
            tradeHistoryViewTheme = tradeHistoryViewThemeFactory.getDeepTealTheme(),
            dialogTheme = dialogThemeFactory.getDeepTealTheme(),
            dottedMapViewTheme = dottedMapViewThemeFactory.getDeepTealTheme(),
            sortPanelTheme = sortPanelThemeFactory.getDeepTealTheme(),
            switchTheme = switchThemeFactory.getDeepTealTheme(),
            popupMenuTheme = popupMenuThemeFactory.getDeepTealTheme(),
            pinEntryKeypadTheme = pinEntryKeypadThemeFactory.getDeepTealTheme(),
            aboutTheme = aboutThemeFactory.getDeepTealTheme()
        )
    }


}