package com.stocksexchange.android.ui.base.useradmission

import com.stocksexchange.android.model.InputViewState
import com.stocksexchange.android.model.UserAdmissionButtonType
import com.stocksexchange.android.ui.base.mvp.views.BaseView

/**
 * A view of the MVP architecture that contains functionality
 * common to user admission views (login, registration,
 * password recovery, etc.).
 */
interface UserAdmissionView<PP : Enum<*>, IV : Enum<*>> : BaseView {


    /**
     * Shows the progress bar of the button specified by the type.
     *
     * @param type The type of the button to show progress bar of
     */
    fun showButtonProgressBar(type: UserAdmissionButtonType)


    /**
     * Hides the progress bar of the button specified by the type.
     *
     * @param type The type of the button to hide progress bar of
     */
    fun hideButtonProgressBar(type: UserAdmissionButtonType)

    
    fun showSecondaryButton()


    fun hideSecondaryButton()


    /**
     * Updates the main view due to a phase change.
     * 
     * @param animate Whether to animate the update process or not
     */
    fun updateMainView(animate: Boolean)

    
    fun finishActivity()

    
    fun setPrimaryButtonText(text: String)

    
    fun setSecondaryButtonText(text: String)


    /**
     * Sets a state of the input views.
     *
     * @param state The state to set
     * @param inputViews The list of input views to set the new state to
     */
    fun setInputViewsState(state: InputViewState, inputViews: List<IV>)


}