package com.stocksexchange.core.utils.extensions

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager


val FragmentManager.visibleFragment: Fragment?
    get() = fragments.firstOrNull { it.isVisible }