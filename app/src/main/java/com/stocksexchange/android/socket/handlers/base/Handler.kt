package com.stocksexchange.android.socket.handlers.base

import com.stocksexchange.android.socket.model.enums.SocketEvent

/**
 * A handler that is responsible for handling socket events.
 */
interface Handler {


    /**
     * A callback that gets called whenever socket data
     * have been received.
     *
     * @param rawSocketData The socket data in raw format
     */
    fun onSocketDataReceived(rawSocketData: Array<out Any>)


    /**
     * A method used for determining whether this handler
     * can handle a particular socket event.
     *
     * @param socketEvent The socket event
     */
    fun canHandleSocketEvent(socketEvent: String): Boolean


    /**
     * Retrieves a socket event type this handler is responsible for.
     *
     * @return The constant from the [SocketEvent] enumeration
     */
    fun getSocketEvent(): SocketEvent


}