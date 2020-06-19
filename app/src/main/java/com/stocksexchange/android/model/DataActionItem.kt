package com.stocksexchange.android.model

/**
 * A model class holding a data item (e.g., an instance of some model class)
 * and an action that was performed or should be performed on it.
 */
data class DataActionItem<T>(val dataItem: T, val action: Action) {


    companion object {


        /**
         * Creates an instance of the [DataActionItem] with the [Action.ANY] action.
         *
         * @param dataItem The data item itself
         *
         * @return The instance of the [DataActionItem]
         */
        fun <T> any(dataItem: T) = DataActionItem(dataItem, Action.ANY)


        /**
         * Creates an instance of the [DataActionItem] with the [Action.INSERT] action.
         *
         * @param dataItem The data item itself
         *
         * @return The instance of the [DataActionItem]
         */
        fun <T> insert(dataItem: T) = DataActionItem(dataItem, Action.INSERT)


        /**
         * Creates an instance of the [DataActionItem] with the [Action.UPDATE] action.
         *
         * @param dataItem The data item itself
         *
         * @return The instance of the [DataActionItem]
         */
        fun <T> update(dataItem: T) = DataActionItem(dataItem, Action.UPDATE)


        /**
         * Creates an instance of the [DataActionItem] with the [Action.REMOVE] action.
         *
         * @param dataItem The data item itself
         *
         * @return The instance of the [DataActionItem]
         */
        fun <T> remove(dataItem: T) = DataActionItem(dataItem, Action.REMOVE)


    }


    enum class Action {

        ANY,
        INSERT,
        UPDATE,
        REMOVE

    }


}