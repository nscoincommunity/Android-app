package com.stocksexchange.android.data.repositories.freshdatahandlers.concrete

import com.stocksexchange.android.data.repositories.freshdatahandlers.interfaces.InboxFreshDataHandler
import com.stocksexchange.api.model.rest.parameters.InboxParameters

class InboxFreshDataHandlerImpl : AdvancedFreshDataHandlerImpl<InboxParameters>(),
    InboxFreshDataHandler