package com.stocksexchange.android.ui.inbox

import com.stocksexchange.api.model.rest.Inbox
import com.stocksexchange.api.model.rest.parameters.InboxParameters
import com.stocksexchange.android.data.repositories.inbox.InboxRepository
import com.stocksexchange.android.model.DataLoadingTrigger
import com.stocksexchange.android.model.DataType
import com.stocksexchange.android.model.RepositoryResult
import com.stocksexchange.android.utils.extensions.log
import com.stocksexchange.android.utils.extensions.onFailure
import com.stocksexchange.android.utils.extensions.onSuccess
import com.stocksexchange.android.ui.base.dataloading.BaseDataLoadingSimpleModel
import com.stocksexchange.android.utils.SavedState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.stocksexchange.android.utils.helpers.tag

class InboxModel(
    private val inboxRepository: InboxRepository
) : BaseDataLoadingSimpleModel<
    List<Inbox>,
    InboxParameters,
    InboxModel.ActionListener
    >() {


    companion object {

        const val REQUEST_TYPE_DELETE_INBOX_ITEM = 0
        const val REQUEST_TYPE_INBOX_READ_ALL = 1

        private val CLASS = InboxModel::class.java

        private val SAVED_STATE_LAST_RESULT_ITEM_COUNT = tag(CLASS, "last_result_item_count")

    }


    private var mLastResultItemCount: Int = -1




    override fun resetParameters() {
        super.resetParameters()

        mLastResultItemCount = -1
    }


    override fun canLoadData(params: InboxParameters, dataType: DataType,
                             dataLoadingTrigger: DataLoadingTrigger): Boolean {
        val isNewData = (dataType == DataType.NEW_DATA)
        val isNewDataWithIntervalNotApplied = (isNewData && !isDataLoadingIntervalApplied())
        val isNetworkConnectivityTrigger = (dataLoadingTrigger == DataLoadingTrigger.NETWORK_CONNECTIVITY)
        val isBottomReachTrigger = (dataLoadingTrigger == DataLoadingTrigger.BOTTOM_REACH)
        val isDataLoaderExhausted = isDataLoaderExhausted(params)

        return ((!isNewDataWithIntervalNotApplied || isNetworkConnectivityTrigger || isBottomReachTrigger) &&
                !isDataLoaderExhausted
        )
    }


    private fun isDataLoaderExhausted(params: InboxParameters): Boolean {
        return ((mLastResultItemCount >= 0) && (mLastResultItemCount < params.limit))
    }


    override suspend fun refreshData(params: InboxParameters) {
        inboxRepository.refresh(params)
    }


    override suspend fun getRepositoryResult(params: InboxParameters): RepositoryResult<List<Inbox>> {
        return inboxRepository.get(params)
            .log(getLogKey(params))
            .onSuccess { withContext(Dispatchers.Main) { handleSuccessfulResponse(it) } }
            .onFailure { withContext(Dispatchers.Main) { handleUnsuccessfulResponse(it) } }
    }

    private fun getLogKey(params: InboxParameters): String {
        return "inboxRepository.get(params: $params)"
    }


    fun deleteInboxItem(id: String) {
        performRequest(
            requestType = REQUEST_TYPE_DELETE_INBOX_ITEM,
            params = id
        )
    }


    fun setInboxReadAll() {
        performRequest(
            requestType = REQUEST_TYPE_INBOX_READ_ALL
        )
    }


    override suspend fun getRequestRepositoryResult(requestType: Int, params: Any): Any? {
        return when (requestType) {
            REQUEST_TYPE_DELETE_INBOX_ITEM -> {
                inboxRepository.deleteInboxItem(params as String).apply {
                    log("deleteInboxItem()")
                }
            }

            REQUEST_TYPE_INBOX_READ_ALL -> {
                inboxRepository.setInboxReadAll().apply {
                    log("setInboxReadAll()")
                }
            }

            else -> throw IllegalStateException()
        }
    }


    override fun handleSuccessfulResponse(data: List<Inbox>) {
        super.handleSuccessfulResponse(data)

        mLastResultItemCount = data.size
    }


    override fun onRestoreState(savedState: SavedState) {
        super.onRestoreState(savedState)

        with(savedState) {
            mLastResultItemCount = get(SAVED_STATE_LAST_RESULT_ITEM_COUNT, -1)
        }
    }


    override fun onSaveState(savedState: SavedState) {
        super.onSaveState(savedState)

        with(savedState) {
            save(SAVED_STATE_LAST_RESULT_ITEM_COUNT, mLastResultItemCount)
        }
    }


    interface ActionListener : BaseDataLoadingActionListener<List<Inbox>>


}