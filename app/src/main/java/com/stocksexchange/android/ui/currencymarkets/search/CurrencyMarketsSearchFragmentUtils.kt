package com.stocksexchange.android.ui.currencymarkets.search

import android.os.Bundle
import androidx.core.os.bundleOf
import com.stocksexchange.android.ui.base.fragments.BaseSearchFragment


fun CurrencyMarketsSearchFragment.Companion.newArgs(
    searchQuery: String = ""
) : Bundle {
    return bundleOf(BaseSearchFragment.KEY_SEARCH_QUERY to searchQuery)
}