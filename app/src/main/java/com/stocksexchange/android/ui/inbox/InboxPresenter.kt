package com.stocksexchange.android.ui.inbox

import com.stocksexchange.api.model.rest.Inbox
import com.stocksexchange.api.model.rest.parameters.InboxParameters
import com.stocksexchange.android.events.InboxNewMessageEvent
import com.stocksexchange.android.model.DataLoadingTrigger
import com.stocksexchange.android.model.DataType
import com.stocksexchange.android.ui.base.listdataloading.BaseListDataLoadingPresenter
import com.stocksexchange.android.utils.SavedState
import com.stocksexchange.android.utils.extensions.extract
import com.stocksexchange.core.handlers.PreferenceHandler
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class InboxPresenter(
    view: InboxContract.View,
    model: InboxModel,
    private val preferenceHandler: PreferenceHandler
) : BaseListDataLoadingPresenter<
    InboxContract.View,
    InboxModel,
    List<Inbox>,
    InboxParameters
    >(view, model), InboxContract.ActionListener, InboxModel.ActionListener {


    private var mSelectedItem: Inbox? = null
    private var mCurrentDeleteItem: Inbox? = null

    private var mInboxParameters: InboxParameters = InboxParameters.getDefaultParameters()




    init {
        model.setActionListener(this)
    }


    private fun updateDataLoadingParams(getNewParams: (InboxParameters) -> InboxParameters) {
        mInboxParameters = getNewParams(getDataLoadingParams())
    }


    override fun resetParameters() {
        super.resetParameters()

        updateDataLoadingParams {
            it.resetOffset()
        }
    }


    override fun getDataTypeForTrigger(trigger: DataLoadingTrigger): DataType {
        return when (trigger) {
            DataLoadingTrigger.START -> DataType.NEW_DATA

            else -> super.getDataTypeForTrigger(trigger)
        }
    }


    override fun getEmptyViewCaption(params: InboxParameters): String {
        return mStringProvider.getInboxEmptyCaption()
    }


    override fun getDataLoadingParams(): InboxParameters {
        return mInboxParameters
    }


    override fun onRefreshData() {
        updateDataLoadingParams {
            it.resetOffset()
        }

        super.onRefreshData()
    }


    override fun onBottomReached(reachedCompletely: Boolean) {
        if(mView.isViewSelected()) {
            updateDataLoadingParams {
                it.copy(offset = mView.getDataSetSize())
            }

            loadData(DataType.OLD_DATA, DataLoadingTrigger.BOTTOM_REACH, true)
        }
    }


    override fun onDataLoadingSucceeded(data: List<Inbox>) {
        super.onDataLoadingSucceeded(data)

        updateDataLoadingParams {
            it.increaseOffset(data.size)
        }
    }


    override fun onItemClicked(inbox: Inbox) {
        if (mSelectedItem?.id == inbox.id) {
            mSelectedItem?.let {
                it.revertItemDetails()
                mView.updateItem(it)
            }

            return
        }

        mSelectedItem?.let {
            it.hideItemDetails()
            mView.updateItem(it)
        }

        inbox.showItemDetails()
        inbox.setItemRead()
        mSelectedItem = inbox
        mView.updateItem(inbox)
    }

    override fun onItemDeleted(inbox: Inbox) {
        mModel.deleteInboxItem(inbox.id)
        mCurrentDeleteItem = inbox
    }


    override fun setInboxAllRead() {
        mModel.setInboxReadAll()
        preferenceHandler.saveInboxUnreadCount(0)
    }


    override fun onRequestSent(requestType: Int) {
        when(requestType) {

            InboxModel.REQUEST_TYPE_DELETE_INBOX_ITEM -> {
                mView.showToolbarProgressBar()
            }

        }
    }


    override fun onResponseReceived(requestType: Int) {
        when(requestType) {

            InboxModel.REQUEST_TYPE_DELETE_INBOX_ITEM -> {
                mView.hideToolbarProgressBar()
                mCurrentDeleteItem?.let {
                    mView.deleteItem(it)
                    mCurrentDeleteItem = null
                }
            }

        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onEvent(event: InboxNewMessageEvent) {
        if(event.isOriginatedFrom(this) || event.isConsumed) {
            return
        }

        mView.addItemToTop(event.inbox)
        event.consume()
    }


    override fun onBackPressed(): Boolean {
        return onNavigateUpPressed()
    }


    override fun canReceiveEvents(): Boolean = true


    override fun onRestoreState(savedState: SavedState) {
        super.onRestoreState(savedState)

        savedState.extract(presenterStateExtractor).also {
            mInboxParameters = it.inboxParameters
        }
    }


    override fun onSaveState(savedState: SavedState) {
        super.onSaveState(savedState)

        savedState.saveState(PresenterState(
            inboxParameters = mInboxParameters
        ))
    }


}