package com.stocksexchange.android.model

enum class PasswordRecoveryProcessPhase {

    AWAITING_EMAIL_ADDRESS,
    PASSWORD_RESET_EMAIL_SENT,

    AWAITING_NEW_PASSWORD,
    PASSWORD_RESET

}