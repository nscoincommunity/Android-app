package com.stocksexchange.android.utils.handlers

import com.stocksexchange.android.BuildConfig
import com.stocksexchange.android.analytics.FirebaseEventLogger
import com.stocksexchange.core.handlers.PreferenceHandler
import com.stocksexchange.core.utils.extensions.hmacSha256
import io.intercom.android.sdk.Intercom
import io.intercom.android.sdk.UnreadConversationCountListener
import io.intercom.android.sdk.identity.Registration

class IntercomHandler(
    private val preferenceHandler: PreferenceHandler,
    private val firebaseEventLogger: FirebaseEventLogger
) {


    /**
     * Registers a visitor, meaning a user that does not have
     * a STEX account.
     */
    fun registerUnidentifiableUser() {
        getClient().registerUnidentifiedUser()

        firebaseEventLogger.onIntercomUnidentifiableUserRegistered()
    }


    /**
     * Registers a user that can be identified by the
     * provided email.
     *
     * @param email The email of the user
     */
    fun registerIdentifiableUser(email: String) {
        with(getClient()) {
            setUserHash(email.hmacSha256(BuildConfig.INTERCOM_API_SECRET))
            registerIdentifiedUser(Registration.create().withEmail(email))
        }

        firebaseEventLogger.onIntercomIdentifiableUserRegistered()
    }


    /**
     * Shows in-app message popups whenever they arrive.
     */
    fun showInAppMessagePopups() {
        getClient().setInAppMessageVisibility(Intercom.Visibility.VISIBLE)
    }


    /**
     * Hides in-app message popups whenever they arrive.
     */
    fun hideInAppMessagePopups() {
        getClient().setInAppMessageVisibility(Intercom.Visibility.GONE)
    }


    fun showMessenger() {
        getClient().displayMessenger()
    }


    fun showHelpCenter() {
        getClient().displayHelpCenter()
    }


    /**
     * Logs out an identifiable user.
     */
    fun logout() {
        getClient().logout()

        preferenceHandler.saveIntercomIdentifiableUserRegistered(false)
    }


    fun addUnreadConversationCountListener(listener: UnreadConversationCountListener) {
        getClient().addUnreadConversationCountListener(listener)
    }


    fun removeUnreadConversationCountListener(listener: UnreadConversationCountListener) {
        getClient().removeUnreadConversationCountListener(listener)
    }


    fun getUnreadConversationCount(): Int {
        return getClient().unreadConversationCount
    }


    private fun getClient(): Intercom = Intercom.client()


}