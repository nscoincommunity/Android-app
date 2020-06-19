package com.stocksexchange.core.utils.interfaces

/**
 * An interface to implement to mark a class to be observable
 * of the network state changes.
 */
interface CanObserveNetworkStateChanges {


    /**
     * This method is called to notify you that the network
     * has been connected.
     */
    fun onNetworkConnected()


    /**
     * This method is called to notify you that the network
     * has been disconnected.
     */
    fun onNetworkDisconnected()


}