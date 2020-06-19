package com.stocksexchange.android.ui.base.dataloading

import com.stocksexchange.android.utils.SavedState


internal val presenterStateExtractor: (SavedState.() -> PresenterState) = {
    PresenterState(
        isRefreshProgressBarEnabled = getOrThrow(PresenterStateKeys.KEY_IS_REFRESH_PROGRESS_BAR_ENABLED),
        isPreviousDataSetEmpty = getOrThrow(PresenterStateKeys.KEY_IS_PREVIOUS_DATA_SET_EMPTY),
        isRealTimeDataUpdateEventReceived = getOrThrow(PresenterStateKeys.KEY_IS_REAL_TIME_DATA_UPDATE_EVENT_RECEIVED)
    )
}


internal val modelStateExtractor: (SavedState.() -> ModelState) = {
    ModelState(
        wasLastDataFetchingSuccessful = getOrThrow(ModelStateKeys.KEY_WAS_LAST_DATA_FETCHING_SUCCESSFUL),
        wasLastDataFetchingInitiatedByUser = getOrThrow(ModelStateKeys.KEY_WAS_LAST_DATA_FETCHING_INITIATED_BY_USER),
        lastDataFetchingTime = getOrThrow(ModelStateKeys.KEY_LAST_DATA_FETCHING_TIME),
        lastDataFetchingException = get(ModelStateKeys.KEY_LAST_DATA_FETCHING_EXCEPTION, null)
    )
}


internal object PresenterStateKeys {

    const val KEY_IS_REFRESH_PROGRESS_BAR_ENABLED = "is_refresh_progress_bar_enabled"
    const val KEY_IS_PREVIOUS_DATA_SET_EMPTY = "is_previous_data_set_empty"
    const val KEY_IS_REAL_TIME_DATA_UPDATE_EVENT_RECEIVED = "is_real_time_data_update_event_received"

}


internal object ModelStateKeys {

    const val KEY_WAS_LAST_DATA_FETCHING_SUCCESSFUL = "was_last_data_fetching_successful"
    const val KEY_WAS_LAST_DATA_FETCHING_INITIATED_BY_USER = "was_last_data_fetching_initiated_by_user"
    const val KEY_LAST_DATA_FETCHING_TIME = "last_data_fetching_time"
    const val KEY_LAST_DATA_FETCHING_EXCEPTION = "last_data_fetching_exception"

}


internal data class PresenterState(
    val isRefreshProgressBarEnabled: Boolean,
    val isPreviousDataSetEmpty: Boolean,
    val isRealTimeDataUpdateEventReceived: Boolean
)


internal data class ModelState (
    val wasLastDataFetchingSuccessful: Boolean,
    val wasLastDataFetchingInitiatedByUser: Boolean,
    val lastDataFetchingTime: Long,
    val lastDataFetchingException: Throwable?
)


internal fun SavedState.saveState(state: PresenterState) {
    save(PresenterStateKeys.KEY_IS_REFRESH_PROGRESS_BAR_ENABLED, state.isRefreshProgressBarEnabled)
    save(PresenterStateKeys.KEY_IS_PREVIOUS_DATA_SET_EMPTY, state.isPreviousDataSetEmpty)
    save(PresenterStateKeys.KEY_IS_REAL_TIME_DATA_UPDATE_EVENT_RECEIVED, state.isRealTimeDataUpdateEventReceived)
}


internal fun SavedState.saveState(state: ModelState) {
    save(ModelStateKeys.KEY_WAS_LAST_DATA_FETCHING_SUCCESSFUL, state.wasLastDataFetchingSuccessful)
    save(ModelStateKeys.KEY_WAS_LAST_DATA_FETCHING_INITIATED_BY_USER, state.wasLastDataFetchingInitiatedByUser)
    save(ModelStateKeys.KEY_LAST_DATA_FETCHING_TIME, state.lastDataFetchingTime)
    save(ModelStateKeys.KEY_LAST_DATA_FETCHING_EXCEPTION, state.lastDataFetchingException)
}