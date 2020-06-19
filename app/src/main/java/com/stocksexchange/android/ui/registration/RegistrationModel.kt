package com.stocksexchange.android.ui.registration

import com.stocksexchange.api.model.rest.parameters.SignUpParameters
import com.stocksexchange.android.data.repositories.useradmission.UserAdmissionRepository
import com.stocksexchange.android.utils.extensions.log
import com.stocksexchange.android.ui.base.useradmission.BaseUserAdmissionModel

class RegistrationModel(
    userAdmissionRepository: UserAdmissionRepository
) : BaseUserAdmissionModel<BaseUserAdmissionModel.BaseActionListener>(userAdmissionRepository) {


    companion object {

        const val REQUEST_TYPE_SIGN_UP = 0
        const val REQUEST_TYPE_ACCOUNT_VERIFICATION_EMAIL_SENDING = 1

    }




    fun performSignUpRequest(params: SignUpParameters) {
        performRequest(
            requestType = REQUEST_TYPE_SIGN_UP,
            params = params
        )
    }


    fun performAccountVerificationEmailSendingRequest(email: String) {
        performRequest(
            requestType = REQUEST_TYPE_ACCOUNT_VERIFICATION_EMAIL_SENDING,
            params = email
        )
    }


    override suspend fun getRequestRepositoryResult(requestType: Int, params: Any): Any? {
        return when(requestType) {

            REQUEST_TYPE_SIGN_UP -> {
                userAdmissionRepository.signUp(params as SignUpParameters).apply {
                    log("userAdmissionRepository.signUp(params: $params)")
                }
            }

            REQUEST_TYPE_ACCOUNT_VERIFICATION_EMAIL_SENDING -> {
                userAdmissionRepository.sendAccountVerificationEmail(params as String).apply {
                    log("userAdmissionRepository.sendAccountVerificationEmail(email: $params)")
                }
            }

            else -> throw IllegalStateException()

        }
    }


}