package com.stocksexchange.android.ui.base.useradmission

import android.os.Parcelable
import com.stocksexchange.android.R
import com.stocksexchange.api.model.rest.UserAdmissionError
import com.stocksexchange.android.model.InputViewState
import com.stocksexchange.android.model.UserAdmissionButtonType
import com.stocksexchange.android.ui.base.mvp.presenters.BasePresenter
import com.stocksexchange.android.utils.SavedState
import com.stocksexchange.android.utils.extensions.extract
import com.stocksexchange.core.utils.helpers.composeInvalidDataDialogMessage
import com.stocksexchange.core.utils.helpers.isEmailAddressInvalid

/**
 * A base presenter of the MVP architecture that contains
 * functionality common for user admission flows (login,
 * registration, etc.).
 */
@Suppress("LeakingThis")
abstract class BaseUserAdmissionPresenter<out V, out M, P, PP, IV>(
    view: V,
    model: M
) : BasePresenter<V, M>(view, model), BaseUserAdmissionModel.BaseActionListener where
        M : BaseUserAdmissionModel<BaseUserAdmissionModel.BaseActionListener>,
        V : UserAdmissionView<PP, IV>,
        P : Parcelable,
        PP : Enum<*>,
        IV : Enum<*> {


    var processPhase: PP = initialProcessPhase

    protected abstract val initialProcessPhase: PP


    /**
     * Data loading parameters.
     */
    protected var mParameters: P = getDefaultParameters()




    init {
        model.setActionListener(this)
    }


    private fun showInvalidDataDialog(errorsList: List<String>) {
        if(errorsList.isEmpty()) {
            mView.showToast(mStringProvider.getSomethingWentWrongMessage())
            return
        }

        val message = composeInvalidDataDialogMessage(
            errorsList,
            mStringProvider.getString(R.string.invalid_data_dialog_footer_text)
        )

        showInfoDialog(
            title = mStringProvider.getString(R.string.invalid_data_dialog_title),
            content = message
        )
    }


    /**
     * Validates the email address.
     *
     * @param emailAddress The email address to validate
     * @param onValid The callback to invoke if the email address
     * happens to be valid
     * @param onInvalid The callback to invoke if the email address
     * happens to be invalid
     */
    protected fun validateEmailAddress(
        emailAddress: String,
        onValid: (() -> Unit)? = null,
        onInvalid: ((MutableList<String>) -> Unit) = { showInvalidDataDialog(it) }
    ) {
        val errorsList = mutableListOf<String>()
        val onFinish: (() -> Unit) = {
            if(errorsList.isEmpty()) {
                onValid?.invoke()
            } else {
                onInvalid(errorsList)
            }
        }

        // Checking if blank
        if(emailAddress.isBlank()) {
            errorsList.add(mStringProvider.getString(R.string.error_empty_email_address))
            return onFinish()
        }

        // Checking if invalid
        if(isEmailAddressInvalid(emailAddress)) {
            errorsList.add(mStringProvider.getString(R.string.error_invalid_email_address))
        }

        return onFinish()
    }


    /**
     * Validates the password.
     *
     * @param password The password to validate
     * @param onValid The callback to invoke if the password happens to be valid
     * @param onInvalid The callback to invoke if the password happens to be invalid
     */
    protected fun validatePassword(
        password: String,
        onValid: (() -> Unit)? = null,
        onInvalid: ((List<String>) -> Unit) = { showInvalidDataDialog(it) }
    ) {
        val errorsList = mutableListOf<String>()
        val onFinish: (() -> Unit) = {
            if(errorsList.isEmpty()) {
                onValid?.invoke()
            } else {
                onInvalid(errorsList)
            }
        }

        // Checking if blank
        if(password.isBlank()) {
            errorsList.add(mStringProvider.getString(R.string.error_empty_password))
            return onFinish()
        }

        return onFinish()
    }


    /**
     * Validates the form by receiving its errors list and input views to highlight
     * as erroneous. If the form has some errors, then the dialog will be shown with
     * respective error messages. Apart from that, erroneous input views will be highlighted.
     *
     * @param errorsList The list of errors of some form
     * @param inputViews The input views to highlight as erroneous
     *
     * @return true if the form is valid (has no errors); false otherwise
     */
    protected fun validateForm(errorsList: List<String>, inputViews: List<IV>): Boolean {
        return if(errorsList.isNotEmpty()) {
            showInvalidDataDialog(errorsList)

            if(inputViews.isNotEmpty()) {
                mView.setInputViewsState(
                    state = InputViewState.ERRONEOUS,
                    inputViews = inputViews
                )
            }

            false
        } else {
            true
        }
    }


    abstract fun getPrimaryButtonText(processPhase: PP): String


    abstract fun getDefaultParameters(): P


    /**
     * Returns a user admission error by checking the provided error message.
     *
     * @param errorMessage The error message
     *
     * @return The user admission error
     */
    protected open fun getUserAdmissionError(errorMessage: String): UserAdmissionError {
        return UserAdmissionError.newInstance(errorMessage)
    }


    open fun onPrimaryButtonClicked() {
        // Stub
    }


    open fun onSecondaryButtonClicked() {
        // Stub
    }


    /**
     * A callback that gets invoked when the new main view
     * is about to be shown.
     */
    open fun onBeforeShowingNewMainView() {
        // Stub
    }


    /**
     * A callback that gets invoked when the new main view
     * has finished showing up.
     */
    open fun onNewMainViewShowingFinished() {
        // Stub
    }


    override fun onRequestSent(requestType: Int) {
        mView.showButtonProgressBar(UserAdmissionButtonType.PRIMARY)
    }


    override fun onResponseReceived(requestType: Int) {
        mView.hideButtonProgressBar(UserAdmissionButtonType.PRIMARY)
    }


    override fun onRequestFailed(requestType: Int, exception: Throwable, metadata: Any) {
        onErrorReceived(exception)
    }


    /**
     * A callback that gets invoked whenever the phase of the user admission process
     * has been changed.
     *
     * @param newProcessPhase The new phase of the process
     * @param shouldAnimateMainViewChange Whether the main view change should be
     * animated or not to reflect the change
     */
    protected open fun onProcessPhaseChanged(newProcessPhase: PP,
                                             shouldAnimateMainViewChange: Boolean = true) {
        this.processPhase = newProcessPhase

        mView.setPrimaryButtonText(getPrimaryButtonText(newProcessPhase))
        mView.updateMainView(shouldAnimateMainViewChange)
    }


    /**
     * A callback that gets invoked whenever a request has delivered an error
     * as its response.
     *
     * @param exception The error to process
     */
    protected open fun onErrorReceived(exception: Throwable) {
        mView.showToast(mStringProvider.getErrorMessage(exception))
    }


    protected fun onMultipleErrorsReceived(errorMessages: List<String>) {
        val localizedErrorMessages = mutableListOf<String>().apply {
            for(errorMessage in errorMessages) {
                val error = getUserAdmissionError(errorMessage)

                if(error != UserAdmissionError.UNKNOWN) {
                    add(mStringProvider.getString(error.stringId))
                }
            }
        }

        showInvalidDataDialog(localizedErrorMessages)
    }


    protected fun onUnknownErrorReceived(message: String) {
        showErrorDialog(message)
    }


    protected fun onUserNotFoundErrorReceived() {
        showErrorDialog(mStringProvider.getString(R.string.error_user_with_email_not_found))
    }


    @Suppress("UNCHECKED_CAST")
    override fun onRestoreState(savedState: SavedState) {
        super.onRestoreState(savedState)

        savedState.extract(presenterStateExtractor).also {
            mParameters = (it.parameters as P)
            processPhase = (it.processPhase as PP)
        }
    }


    override fun onSaveState(savedState: SavedState) {
        super.onSaveState(savedState)

        savedState.saveState(PresenterState(
            parameters = mParameters,
            processPhase = processPhase
        ))
    }


}