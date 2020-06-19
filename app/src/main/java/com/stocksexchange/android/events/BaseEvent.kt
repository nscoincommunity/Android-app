package com.stocksexchange.android.events

import com.stocksexchange.android.utils.helpers.tag
import org.greenrobot.eventbus.EventBus

abstract class BaseEvent<out Attachment>(
    val type: Int,
    val attachment: Attachment,
    val sourceTag: String,
    var consumerCount: Int = 0,
    var isConsumed: Boolean = false,
    var onConsumeListener: OnConsumeListener? = null
) {


    companion object {

        const val TYPE_INVALID = -1
        const val TYPE_SINGLE_ITEM = 0
        const val TYPE_MULTIPLE_ITEMS = 1

    }


    private val consumers: HashSet<String> = hashSetOf()




    /**
     * Consumes the event at once.
     */
    fun consume() {
        if(!isConsumed) {
            consumeInternal()
        }
    }


    /**
     * Adds a specified consumer to the set of consumers and
     * removes the event if the it becomes exhausted afterwards.
     *
     * @param consumer The consumer to add
     */
    fun consume(consumer: Any) {
        if(consumerCount == 0) {
            consume()
            return
        }

        consumers.add(tag(consumer))

        if(isExhausted()) {
            consumeInternal()
        }
    }


    private fun consumeInternal() {
        EventBus.getDefault().removeStickyEvent(this)
        isConsumed = true

        onConsumeListener?.onConsumed()
    }


    /**
     * Checks whether this event has already been consumed
     * by the specified consumer.
     *
     * @return true if has been; false otherwise
     */
    fun isConsumed(consumer: Any): Boolean {
        return consumers.contains(tag(consumer))
    }


    /**
     * Checks whether all consumers have consumed this event.
     *
     * @return true if this event has been consumed by all consumers;
     * false otherwise
     */
    fun isExhausted(): Boolean {
        return (consumerCount == consumers.size)
    }


    /**
     * Checks this event has been originated (created) by the passed source.
     *
     * @param source The source to check against
     *
     * @return true if the event was originated from the source; false otherwise
     */
    fun isOriginatedFrom(source: Any): Boolean {
        return (sourceTag.isNotBlank() && (sourceTag == tag(source)))
    }


    /**
     * Returns how many consumers have already consumed this event.
     *
     * @return A number that denotes how many consumers have already
     * consumed this event
     */
    fun getCurrentConsumerCount(): Int {
        return consumers.size
    }


    /**
     * A listener that allows to get notified whenever an event
     * is consumed.
     */
    interface OnConsumeListener {

        fun onConsumed()

    }


}