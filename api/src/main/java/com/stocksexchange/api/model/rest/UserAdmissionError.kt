package com.stocksexchange.api.model.rest

import androidx.annotation.StringRes
import com.stocksexchange.api.R

enum class UserAdmissionError(val message: String, @StringRes val stringId: Int) {


    EMAIL_TAKEN(
        message = "The email has already been taken.",
        stringId = R.string.error_email_taken
    ),
    PASSWORD_LENGTH(
        message = "The password must be between 8 and 32 characters.",
        stringId = R.string.error_password_length
    ),
    PASSWORD_COMMON(
        message = "This password is just too common. Please try another!",
        stringId = R.string.error_password_common
    ),
    INVALID_VERIFICATION_TOKEN(
        message = "Invalid verification token!",
        stringId = R.string.error_invalid_verification_token
    ),
    INVALID_PASSWORD_RESET_TOKEN(
        message = "Invalid token.",
        stringId = R.string.error_invalid_password_reset_token
    ),

    UNKNOWN(
        message = "Unknown error.",
        stringId = R.string.unknown_error
    );


    companion object {

        val REGISTRATION_ERRORS = arrayOf(EMAIL_TAKEN, PASSWORD_LENGTH, PASSWORD_COMMON)
        val ACCOUNT_VERIFICATION_ERRORS = arrayOf(INVALID_VERIFICATION_TOKEN)
        val PASSWORD_RECOVERY_ERRORS = arrayOf(PASSWORD_LENGTH, PASSWORD_COMMON, INVALID_PASSWORD_RESET_TOKEN)
        val LOGIN_ERRORS = arrayOf(PASSWORD_LENGTH, PASSWORD_COMMON)


        fun newInstance(message: String): UserAdmissionError {
            return newInstance(message, values())
        }


        fun newRegistrationError(message: String): UserAdmissionError {
            return newInstance(message, REGISTRATION_ERRORS)
        }


        fun newAccountVerificationError(message: String): UserAdmissionError {
            return newInstance(message, ACCOUNT_VERIFICATION_ERRORS)
        }


        fun newPasswordRecoveryError(message: String): UserAdmissionError {
            return newInstance(message, PASSWORD_RECOVERY_ERRORS)
        }


        fun newLoginError(message: String): UserAdmissionError {
            return newInstance(message, LOGIN_ERRORS)
        }


        private fun newInstance(
            message: String,
            possibleErrors: Array<UserAdmissionError>
        ): UserAdmissionError {
            for(error in possibleErrors) {
                if(message == error.message) {
                    return error
                }
            }

            return UNKNOWN
        }

    }


}