package com.stocksexchange.android.ui.login

import com.stocksexchange.api.model.rest.parameters.SignInParameters
import com.stocksexchange.android.data.repositories.profileinfos.ProfileInfosRepository
import com.stocksexchange.android.data.repositories.settings.SettingsRepository
import com.stocksexchange.android.model.Settings
import com.stocksexchange.android.data.repositories.useradmission.UserAdmissionRepository
import com.stocksexchange.android.utils.extensions.log
import com.stocksexchange.android.ui.base.useradmission.BaseUserAdmissionModel

class LoginModel(
    userAdmissionRepository: UserAdmissionRepository,
    private val profileInfosRepository: ProfileInfosRepository,
    private val settingsRepository: SettingsRepository
) : BaseUserAdmissionModel<BaseUserAdmissionModel.BaseActionListener>(userAdmissionRepository) {


    companion object {

        const val REQUEST_TYPE_SIGN_IN = 0
        const val REQUEST_TYPE_ACCOUNT_VERIFICATION_EMAIL_SENDING = 1
        const val REQUEST_TYPE_SIGN_IN_CONFIRMATION = 2
        const val REQUEST_TYPE_PROFILE_INFO = 3

    }




    fun performSignInRequest(params: SignInParameters) {
        performRequest(
            requestType = REQUEST_TYPE_SIGN_IN,
            params = params
        )
    }


    fun performAccountVerificationEmailSendingRequest(email: String) {
        performRequest(
            requestType = REQUEST_TYPE_ACCOUNT_VERIFICATION_EMAIL_SENDING,
            params = email
        )
    }


    fun performSignInConfirmationRequest(params: SignInParameters) {
        performRequest(
            requestType = REQUEST_TYPE_SIGN_IN_CONFIRMATION,
            params = params
        )
    }


    fun performProfileInfoFetchingRequest(email: String) {
        performRequest(
            requestType = REQUEST_TYPE_PROFILE_INFO,
            params = email
        )
    }


    override suspend fun getRequestRepositoryResult(requestType: Int, params: Any): Any? {
        return when(requestType) {

            REQUEST_TYPE_SIGN_IN -> {
                userAdmissionRepository.signIn(params as SignInParameters).apply {
                    log("userAdmissionRepository.signIn(params: $params)")
                }
            }

            REQUEST_TYPE_ACCOUNT_VERIFICATION_EMAIL_SENDING -> {
                userAdmissionRepository.sendAccountVerificationEmail(params as String).apply {
                    log("userAdmissionRepository.sendAccountVerificationEmail(email: $params)")
                }
            }

            REQUEST_TYPE_SIGN_IN_CONFIRMATION -> {
                userAdmissionRepository.confirmSignIn(params as SignInParameters).apply {
                    log("userAdmissionRepository.confirmSignIn(params: $params)")
                }
            }

            REQUEST_TYPE_PROFILE_INFO -> {
                // Refreshing to retrieve the most up-to-date data
                profileInfosRepository.refresh()

                // Actual fetching
                profileInfosRepository.get(params as String).apply {
                    log("profileInfosRepository.get(email: $params)")
                }
            }

            else -> throw IllegalStateException()

        }
    }


    fun updateSettings(settings: Settings, onFinish: (() -> Unit)) {
        createUiLaunchCoroutine {
            settingsRepository.save(settings)

            onFinish()
        }
    }


}