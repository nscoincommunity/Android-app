package com.stocksexchange.android.ui.auth

import com.stocksexchange.android.model.Settings
import com.stocksexchange.android.data.repositories.settings.SettingsRepository
import com.stocksexchange.android.ui.auth.AuthenticationModel.ActionListener
import com.stocksexchange.android.ui.base.mvp.model.BaseModel
import com.stocksexchange.android.utils.SavedState
import com.stocksexchange.android.utils.helpers.tag
import com.stocksexchange.core.handlers.PreferenceHandler
import com.stocksexchange.core.utils.EncryptionUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthenticationModel(
    private val encryptionUtil: EncryptionUtil,
    private val preferenceHandler: PreferenceHandler,
    private val settingsRepository: SettingsRepository
) : BaseModel<ActionListener>() {


    companion object {

        const val MAX_INVALID_PIN_CODE_ATTEMPTS_NUMBER = 5

        const val TIMER_DURATION = 30_000L
        const val TIMER_INTERVAL = 1_000L
        const val TIMER_MIN_FINISH_TIME = 1_000L

        private val CLASS = AuthenticationModel::class.java

        private val KEY_ARE_FINGERPRINT_ATTEMPTS_USED_UP = tag(CLASS, "are_fingerprint_attempts_used_up")
        private val KEY_INVALID_PIN_CODE_ATTEMPTS_NUMBER = tag(CLASS, "invalid_pin_code_attempts_number")
        private val KEY_ALLOW_AUTH_TIMESTAMP = tag(CLASS, "allow_auth_timestamp")

    }


    private var mAreFingerprintAttemptsUsedUp: Boolean = false

    private var mInvalidPinCodeAttemptsNumber: Int = 0

    private var mAllowAuthTimestamp: Long = 0L




    fun initAuthVariables(onFinish: (() -> Unit)) {
        createBgLaunchCoroutine {
            mAreFingerprintAttemptsUsedUp = if(preferenceHandler.hasFingerprintAttemptsUsedUp()) {
                encryptionUtil.decrypt(preferenceHandler.areFingerprintAttemptsUsedUp())
                    .takeIf { it.isNotBlank() }
                    ?.toBoolean()
                    ?: true
            } else {
                false
            }

            mInvalidPinCodeAttemptsNumber = if(preferenceHandler.hasInvalidPinCodeAttemptsNumber()) {
                encryptionUtil.decrypt(preferenceHandler.getInvalidPinCodeAttemptsNumber())
                    .takeIf { it.isNotBlank() }
                    ?.toInt()
                    ?: MAX_INVALID_PIN_CODE_ATTEMPTS_NUMBER
            } else {
                0
            }

            mAllowAuthTimestamp = if(preferenceHandler.hasAllowAuthTime()) {
                encryptionUtil.decrypt(preferenceHandler.getAllowAuthTimestamp())
                    .takeIf { it.isNotBlank() }
                    ?.toLong()
                    ?: generateNewAllowAuthTimestamp()
            } else {
                0L
            }

            withContext(Dispatchers.Main) {
                onFinish()
            }
        }
    }


    fun saveLastAuthTimestamp(lastAuthTimestamp: Long, onFinish: () -> Unit) {
        createBgLaunchCoroutine {
            preferenceHandler.saveLastAuthTimestamp(encryptionUtil.encrypt(
                lastAuthTimestamp.toString()
            ))

            withContext(Dispatchers.Main) {
                onFinish()
            }
        }
    }


    fun deleteAllowAuthTimestamp() {
        preferenceHandler.removeAllowAuthTimestamp()
    }


    fun updateAllowAuthTimestamp() {
        mAllowAuthTimestamp = generateNewAllowAuthTimestamp()

        createBgLaunchCoroutine {
            preferenceHandler.saveAllowAuthTimestamp(encryptionUtil.encrypt(
                mAllowAuthTimestamp.toString()
            ))
        }
    }


    fun incrementInvalidPinCodeAttemptsNumber() {
        mInvalidPinCodeAttemptsNumber++

        createBgLaunchCoroutine {
            preferenceHandler.saveInvalidPinCodeAttemptsNumber(encryptionUtil.encrypt(
                mInvalidPinCodeAttemptsNumber.toString()
            ))
        }
    }


    fun resetAuthData() {
        mAreFingerprintAttemptsUsedUp = false
        mInvalidPinCodeAttemptsNumber = 0

        preferenceHandler.removeFingerprintAttemptsUsedUp()
        preferenceHandler.removeInvalidPinCodeAttemptsNumber()
    }


    private fun generateNewAllowAuthTimestamp(): Long {
        return (System.currentTimeMillis() + TIMER_DURATION)
    }


    fun updateSettings(settings: Settings, onFinish: () -> Unit) {
        createUiLaunchCoroutine {
            settingsRepository.save(settings)

            onFinish()
        }
    }


    fun setFingerprintAttemptsUsedUp(areFingerprintAttemptsUsedUp: Boolean) {
        mAreFingerprintAttemptsUsedUp = areFingerprintAttemptsUsedUp

        createBgLaunchCoroutine {
            preferenceHandler.saveFingerprintAttemptsUsedUp(encryptionUtil.encrypt(
                areFingerprintAttemptsUsedUp.toString()
            ))
        }
    }


    fun areFingerprintAttemptsUsedUp(): Boolean {
        return mAreFingerprintAttemptsUsedUp
    }


    fun getInvalidPinCodeAttemptsNumber(): Int {
        return mInvalidPinCodeAttemptsNumber
    }


    fun getAllowAuthTimestamp(): Long {
        return mAllowAuthTimestamp
    }


    override fun onRestoreState(savedState: SavedState) {
        super.onRestoreState(savedState)

        with(savedState) {
            mAreFingerprintAttemptsUsedUp = get(KEY_ARE_FINGERPRINT_ATTEMPTS_USED_UP, false)
            mInvalidPinCodeAttemptsNumber = get(KEY_INVALID_PIN_CODE_ATTEMPTS_NUMBER, 0)
            mAllowAuthTimestamp = get(KEY_ALLOW_AUTH_TIMESTAMP, 0L)
        }
    }


    override fun onSaveState(savedState: SavedState) {
        super.onSaveState(savedState)

        with(savedState) {
            save(KEY_ARE_FINGERPRINT_ATTEMPTS_USED_UP, mAreFingerprintAttemptsUsedUp)
            save(KEY_INVALID_PIN_CODE_ATTEMPTS_NUMBER, mInvalidPinCodeAttemptsNumber)
            save(KEY_ALLOW_AUTH_TIMESTAMP, mAllowAuthTimestamp)
        }
    }


    interface ActionListener : BaseActionListener


}