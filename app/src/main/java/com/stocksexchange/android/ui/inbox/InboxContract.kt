package com.stocksexchange.android.ui.inbox

import com.stocksexchange.api.model.rest.Inbox
import com.stocksexchange.android.ui.base.listdataloading.ListDataLoadingView

interface InboxContract {


    interface View : ListDataLoadingView<List<Inbox>> {

        fun updateItem(item: Inbox)

        fun deleteItem(item: Inbox)

        fun updateInboxCountUnreadItem()

        fun getItemPosition(item: Inbox): Int?

        fun getUnreadCountItemOnScreen(): Int

        fun addItemToTop(item: Inbox)

        fun showToolbarProgressBar()

        fun hideToolbarProgressBar()

    }


    interface ActionListener {

        fun onItemClicked(inbox: Inbox)

        fun onItemDeleted(inbox: Inbox)

        fun setInboxAllRead()

    }


}