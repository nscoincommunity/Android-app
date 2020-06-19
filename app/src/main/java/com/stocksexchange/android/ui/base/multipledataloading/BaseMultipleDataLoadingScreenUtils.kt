package com.stocksexchange.android.ui.base.multipledataloading

import com.stocksexchange.android.utils.SavedState


internal val presenterStateExtractor: (SavedState.() -> PresenterState) = {
    PresenterState(
        isRealTimeDataUpdateEventReceived = getOrThrow(PresenterStateKeys.KEY_IS_REAL_TIME_DATA_UPDATE_EVENT_RECEIVED),
        isDataLoadingPerformedMap = getOrThrow(PresenterStateKeys.KEY_IS_DATA_LOADING_PERFORMED_MAP)
    )
}


internal val modelStateExtractor: (SavedState.() -> ModelState) = {
    ModelState(
        lastDataFetchingTimeMap = getOrThrow(ModelStateKeys.KEY_LAST_DATA_FETCHING_TIME_MAP)
    )
}



internal object PresenterStateKeys {

    const val KEY_IS_REAL_TIME_DATA_UPDATE_EVENT_RECEIVED = "is_real_time_data_update_event_received"
    const val KEY_IS_DATA_LOADING_PERFORMED_MAP = "is_data_loading_performed_map"

}


internal object ModelStateKeys {

    const val KEY_LAST_DATA_FETCHING_TIME_MAP = "last_data_fetching_time_map"

}


internal data class PresenterState(
    val isRealTimeDataUpdateEventReceived: Boolean,
    val isDataLoadingPerformedMap: MutableMap<Any, Boolean>
)


internal data class ModelState(
    val lastDataFetchingTimeMap: MutableMap<Any, Long>
)


internal fun SavedState.saveState(state: PresenterState) {
    save(PresenterStateKeys.KEY_IS_REAL_TIME_DATA_UPDATE_EVENT_RECEIVED, state.isRealTimeDataUpdateEventReceived)
    save(PresenterStateKeys.KEY_IS_DATA_LOADING_PERFORMED_MAP, state.isDataLoadingPerformedMap)
}


internal fun SavedState.saveState(state: ModelState) {
    save(ModelStateKeys.KEY_LAST_DATA_FETCHING_TIME_MAP, state.lastDataFetchingTimeMap)
}