package com.stocksexchange.android.ui.base.useradmission

import com.stocksexchange.android.data.repositories.useradmission.UserAdmissionRepository
import com.stocksexchange.android.ui.base.mvp.model.BaseModel

/**
 * A base model of the MVP architecture that contains
 * code common for user admission flows (login, registration,
 * etc.).
 */
abstract class BaseUserAdmissionModel<
    ActionListener : BaseUserAdmissionModel.BaseActionListener
>(
    protected val userAdmissionRepository: UserAdmissionRepository
) : BaseModel<ActionListener>() {


    interface BaseActionListener : BaseModel.BaseActionListener


}