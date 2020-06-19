package com.stocksexchange.android.socket.model

data class SocketState(
    private val channelSubscribersMap: MutableMap<String, MutableSet<String>> = mutableMapOf()
) : Iterable<MutableMap.MutableEntry<String, MutableSet<String>>> {


    fun addSubscriberToChannel(subscriberKey: String, channel: String) {
        val subscriberKeys = (channelSubscribersMap[channel] ?: mutableSetOf()).apply {
            add(subscriberKey)
        }

        channelSubscribersMap[channel] = subscriberKeys
    }


    fun removeSubscriberFromChannel(subscriberKey: String, channel: String) {
        val subscriberKeys = (channelSubscribersMap[channel] ?: return).apply {
            remove(subscriberKey)
        }

        if(subscriberKeys.isEmpty()) {
            channelSubscribersMap.remove(channel)
        } else {
            channelSubscribersMap[channel] = subscriberKeys
        }
    }


    fun save(socketState: SocketState) {
        channelSubscribersMap.putAll(socketState.channelSubscribersMap)
    }


    fun doesChannelHaveSubscribers(channel: String): Boolean {
        return (channel in channelSubscribersMap)
    }


    fun isEmpty(): Boolean {
        return channelSubscribersMap.isEmpty()
    }


    fun getSubscriberCountForChannel(channel: String): Int {
        return (channelSubscribersMap[channel]?.size ?: 0)
    }


    fun getActiveChannels(): Set<String> {
        return channelSubscribersMap.keys
    }


    fun clear() {
        channelSubscribersMap.clear()
    }


    override fun iterator(): Iterator<MutableMap.MutableEntry<String, MutableSet<String>>> {
        return channelSubscribersMap.iterator()
    }


}