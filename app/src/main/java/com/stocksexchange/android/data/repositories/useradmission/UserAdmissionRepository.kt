package com.stocksexchange.android.data.repositories.useradmission

import com.stocksexchange.api.model.rest.*
import com.stocksexchange.android.data.base.UserAdmissionData
import com.stocksexchange.android.data.repositories.base.Repository
import com.stocksexchange.android.model.RepositoryResult

interface UserAdmissionRepository : UserAdmissionData<
    RepositoryResult<SignUpResponse>,
    RepositoryResult<AccountVerificationEmailSendingResponse>,
    RepositoryResult<AccountVerificationResponse>,
    RepositoryResult<PasswordResetEmailSendingResponse>,
    RepositoryResult<PasswordResetResponse>,
    RepositoryResult<SignInResponse>,
    RepositoryResult<SignInConfirmationResponse>,
    RepositoryResult<OAuthCredentials>
>, Repository