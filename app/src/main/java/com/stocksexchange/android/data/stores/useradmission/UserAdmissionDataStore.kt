package com.stocksexchange.android.data.stores.useradmission

import com.stocksexchange.api.model.rest.*
import com.stocksexchange.android.data.base.UserAdmissionData
import com.stocksexchange.core.model.Result

interface UserAdmissionDataStore : UserAdmissionData<
    Result<SignUpResponse>,
    Result<AccountVerificationEmailSendingResponse>,
    Result<AccountVerificationResponse>,
    Result<PasswordResetEmailSendingResponse>,
    Result<PasswordResetResponse>,
    Result<SignInResponse>,
    Result<SignInConfirmationResponse>,
    Result<OAuthCredentials>
>