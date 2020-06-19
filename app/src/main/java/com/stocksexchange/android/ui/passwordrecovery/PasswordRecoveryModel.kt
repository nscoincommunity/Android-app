package com.stocksexchange.android.ui.passwordrecovery

import com.stocksexchange.api.model.rest.parameters.PasswordRecoveryParameters
import com.stocksexchange.android.data.repositories.useradmission.UserAdmissionRepository
import com.stocksexchange.android.utils.extensions.log
import com.stocksexchange.android.ui.base.useradmission.BaseUserAdmissionModel

class PasswordRecoveryModel(
    userAdmissionRepository: UserAdmissionRepository
) : BaseUserAdmissionModel<BaseUserAdmissionModel.BaseActionListener>(userAdmissionRepository) {


    companion object {

        const val REQUEST_TYPE_SEND_PASSWORD_RESET_EMAIL = 0
        const val REQUEST_TYPE_RESET_PASSWORD = 1

    }




    fun performPasswordResetEmailSendingRequest(email: String) {
        performRequest(
            requestType = REQUEST_TYPE_SEND_PASSWORD_RESET_EMAIL,
            params = email
        )
    }


    fun performPasswordResetRequest(params: PasswordRecoveryParameters) {
        performRequest(
            requestType = REQUEST_TYPE_RESET_PASSWORD,
            params = params
        )
    }


    override suspend fun getRequestRepositoryResult(requestType: Int, params: Any): Any? {
        return when(requestType) {

            REQUEST_TYPE_SEND_PASSWORD_RESET_EMAIL -> {
                userAdmissionRepository.sendPasswordResetEmail(params as String).apply {
                    log("userAdmissionRepository.sendPasswordResetEmail(params: $params)")
                }
            }

            REQUEST_TYPE_RESET_PASSWORD -> {
                userAdmissionRepository.resetPassword(params as PasswordRecoveryParameters).apply {
                    log("userAdmissionRepository.resetPassword(params: $params)")
                }
            }

            else -> throw IllegalStateException()

        }
    }


}