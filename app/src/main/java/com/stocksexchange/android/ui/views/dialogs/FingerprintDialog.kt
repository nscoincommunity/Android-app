package com.stocksexchange.android.ui.views.dialogs

import android.content.Context
import android.content.DialogInterface
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.view.View
import android.view.WindowManager
import androidx.annotation.ColorInt
import androidx.core.hardware.fingerprint.FingerprintManagerCompat
import androidx.core.os.CancellationSignal
import com.stocksexchange.android.R
import com.stocksexchange.android.ui.views.dialogs.base.BaseStandardDialog
import com.stocksexchange.core.utils.extensions.*
import com.stocksexchange.core.utils.extensions.makeGone
import kotlinx.android.synthetic.main.fingerprint_dialog_layout.*
import java.security.KeyStore
import java.util.UUID
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

/**
 * A dialog responsible for authenticating user with a fingerprint.
 */
class FingerprintDialog constructor(
    context: Context,
    mode: Mode,
    listener: Listener,
    styleResourceId: Int = R.style.DefaultDialogTheme
) : BaseStandardDialog(context, styleResourceId) {


    companion object {

        private val KEY_NAME = UUID.randomUUID().toString()

        private const val SUCCESS_DELAY_IN_MILLIS = 1500L

        private const val ERROR_WITH_DETAILS_DELAY_IN_MILLIS = 3500L
        private const val ERROR_WITHOUT_DETAILS_DELAY_IN_MILLIS = 2500L

        private const val RESET_ERROR_DELAY_IN_MILLIS = 1600L

        private const val TOO_MANY_ATTEMPTS_ERROR_CODE = 7

    }


    private var mIsInitialized: Boolean = false
    private var mIsScanning: Boolean = false
    private var mIsSuccessPending: Boolean = false
    private var mIsErrorPending: Boolean = false

    private var mBackgroundColor: Int = 0
    private var mTitleTextColor: Int = 0
    private var mSubtitleTextColor: Int = 0
    private var mDescriptionTextColor: Int = 0
    private var mStatusTextColor: Int = 0
    private var mButtonTextColor: Int = 0
    private var mSuccessColor: Int = 0
    private var mErrorColor: Int = 0

    private var mSubtitleText: String = ""
    private var mButtonText: String = ""

    private var mSuccessDrawable: Drawable? = null
    private var mErrorDrawable: Drawable? = null

    private var mLastFatalError: Error? = null

    private val mMode: Mode = mode

    private var mKeyStore: KeyStore? = null

    private var mCipher: Cipher? = null

    private var mCancellationSignal: CancellationSignal? = null

    private val mDelayedTasks: MutableList<Runnable> = mutableListOf()

    private var mListener: Listener = listener
    private var mButtonListener: ((View) -> Unit)? = null




    init {
        prefetchResources()
    }


    private fun prefetchResources() {
        mSuccessColor = getColor(R.color.colorFingerprintSuccess)
        mErrorColor = getColor(R.color.colorFingerprintError)

        mSuccessDrawable = getDrawable(R.drawable.ic_fingerprint_success)
        mErrorDrawable = getDrawable(R.drawable.ic_fingerprint_error)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.fingerprint_dialog_layout)
        init()

        mIsInitialized = true
    }


    private fun init() {
        initBackground()
        initTitle()
        initSubtitle()
        initDescription()
        initStatus()
        initButton()
        initWindow()
        initListeners()

        executeDelayedTasks()
    }


    private fun initBackground() {
        setBackgroundColor(mBackgroundColor)
    }


    private fun initTitle() {
        with(titleTv) {
            text = mStringProvider.getString(R.string.fingerprint_dialog_title)
            setTitleTextColor(mTitleTextColor)
        }
    }


    private fun initSubtitle() {
        setSubtitleText(mSubtitleText)
        setSubtitleTextColor(mSubtitleTextColor)
    }


    private fun initDescription() {
        with(descriptionTv) {
            text = mStringProvider.getString(R.string.fingerprint_dialog_description)
            setDescriptionTextColor(mDescriptionTextColor)
        }
    }


    private fun initStatus() {
        with(statusTv) {
            text = mStringProvider.getString(R.string.fingerprint_dialog_default_status_text)
            setStatusTextColor(mStatusTextColor)
        }
    }


    private fun initButton() {
        setButtonText(mButtonText)
        setButtonTextColor(mButtonTextColor)
        setButtonListener(mButtonListener)
    }


    private fun initWindow() {
        window?.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
    }


    private fun initListeners() {
        setOnDismissListener(this)
    }


    @SuppressWarnings("NewApi")
    private fun generateKey(): Boolean {
        mKeyStore = null
        val keyGenerator: KeyGenerator

        try {
            mKeyStore = KeyStore.getInstance("AndroidKeyStore")
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
        } catch(e: Exception) {
            return false
        }

        return try {
            mKeyStore?.load(null)

            val params = KeyGenParameterSpec.Builder(
                KEY_NAME,
                (KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
            ).apply {
                setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                setUserAuthenticationRequired(true)
                setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
            }.build()

            keyGenerator.init(params)
            keyGenerator.generateKey()

            true
        } catch(e: Exception) {
            false
        }
    }


    @SuppressWarnings("NewApi")
    private fun initCipher(): Boolean {
        if(!generateKey()) {
            return false
        }

        try {
            val keyAlgorithm = KeyProperties.KEY_ALGORITHM_AES
            val blockMode = KeyProperties.BLOCK_MODE_CBC
            val encryptionPadding = KeyProperties.ENCRYPTION_PADDING_PKCS7

            mCipher = Cipher.getInstance("$keyAlgorithm/$blockMode/$encryptionPadding")
        } catch(e: Exception) {
            return false
        }

        return try {
            mKeyStore?.load(null)
            mCipher!!.init(Cipher.ENCRYPT_MODE, mKeyStore!!.getKey(KEY_NAME, null) as SecretKey)

            true
        } catch(e: Exception) {
            false
        }
    }


    @SuppressWarnings("NewApi")
    private fun getCryptoObject(): FingerprintManagerCompat.CryptoObject? {
        return if(initCipher()) FingerprintManagerCompat.CryptoObject(mCipher!!) else null
    }


    private fun displayError(error: CharSequence, postResetRunnable: Boolean = true) {
        iconIv.setImageDrawable(mErrorDrawable)

        statusTv.setTextColor(mErrorColor)
        statusTv.text = error

        if(postResetRunnable) {
            removeRunnable(mResetErrorRunnable)
            postDelayed(mResetErrorRunnable, RESET_ERROR_DELAY_IN_MILLIS)
        }
    }


    private fun displayErrorAndFinish(error: Error, errorText: CharSequence,
                                      delay: Long) {
        hideDescription()
        displayError(errorText, false)

        mLastFatalError = error

        postDelayed(
            mErrorRunnable,
            delay
        )
    }


    private fun postDelayed(action: Runnable, delay: Long) {
        cardView.postDelayed(action, delay)
    }


    private fun removeRunnable(runnable: Runnable) {
        cardView.removeCallbacks(runnable)
    }


    private fun executeTask(task: (() -> Unit)) {
        if(mIsInitialized) {
            task()
        } else {
            mDelayedTasks.add(Runnable {
                task()
            })
        }
    }


    private fun executeDelayedTasks() {
        mDelayedTasks.forEach {
            it.run()
        }

        mDelayedTasks.clear()
    }


    fun startAuthenticationProcess() {
        if(mIsScanning) {
            stopAuthenticationProcess()
        }

        val fingerprintManager = context.getFingerprintManager()
        val cryptoObject = getCryptoObject()

        if(cryptoObject == null) {
            executeTask {
                displayErrorAndFinish(
                    Error.FAILED_TO_INITIALIZE_FINGERPRINT_SCANNER,
                    mStringProvider.getString(R.string.error_fingerprint_initialization_failed),
                    ERROR_WITHOUT_DETAILS_DELAY_IN_MILLIS
                )
            }

            return
        }

        mCancellationSignal = CancellationSignal()
        fingerprintManager.authenticate(cryptoObject, 0, mCancellationSignal, mFingerprintListener, null)

        mIsScanning = true
    }


    fun stopAuthenticationProcess() {
        if(mCancellationSignal != null) {
            mIsScanning = false

            mCancellationSignal?.cancel()
            mCancellationSignal = null
        }
    }


    fun showSubtitle() {
        executeTask {
            subtitleTv.makeVisible()
        }
    }


    fun hideSubtitle() {
        executeTask {
            subtitleTv.makeGone()
        }
    }


    fun showDescription() {
        executeTask {
            descriptionTv.makeVisible()
        }
    }


    fun hideDescription() {
        executeTask {
            descriptionTv.makeGone()
        }
    }


    fun setBackgroundColor(@ColorInt color: Int) {
        mBackgroundColor = color

        cardView?.setCardBackgroundColor(color)
    }


    fun setTitleTextColor(@ColorInt color: Int) {
        mTitleTextColor = color

        titleTv?.setTextColor(color)
    }


    fun setSubtitleTextColor(@ColorInt color: Int) {
        mSubtitleTextColor = color

        subtitleTv?.setTextColor(color)
    }


    fun setDescriptionTextColor(@ColorInt color: Int) {
        mDescriptionTextColor = color

        descriptionTv?.setTextColor(color)
    }


    fun setStatusTextColor(@ColorInt color: Int) {
        mStatusTextColor = color

        statusTv?.setTextColor(color)
    }


    fun setButtonTextColor(@ColorInt color: Int) {
        mButtonTextColor = color

        buttonTv?.setTextColor(color)
    }


    fun setSubtitleText(text: String) {
        mSubtitleText = text

        subtitleTv?.text = text
    }


    fun setButtonText(text: String) {
        mButtonText = text

        buttonTv?.text = text
    }


    fun setButtonListener(listener: ((View) -> Unit)?) {
        mButtonListener = listener

        buttonTv?.setOnClickListener(listener)
    }


    override fun onDismiss(dialog: DialogInterface?) {
        if(mIsSuccessPending) {
            removeRunnable(mSuccessRunnable)
            onSuccess()
        }

        if(mIsErrorPending) {
            removeRunnable(mErrorRunnable)
            onError(mLastFatalError!!)
        }

        if(!mIsSuccessPending && !mIsErrorPending) {
            mListener.onDismiss()
        }

        stopAuthenticationProcess()
    }


    private fun onSuccess() {
        if(!mIsScanning) {
            return
        }

        mListener.onSuccess()
        dismiss()
    }


    private fun onError(error: Error) {
        if(!mIsScanning) {
            return
        }

        mListener.onError(error)
        dismiss()
    }


    @SuppressWarnings("NewApi")
    private val mFingerprintListener: FingerprintManagerCompat.AuthenticationCallback =
        object : FingerprintManagerCompat.AuthenticationCallback() {

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                removeRunnable(mResetErrorRunnable)

                if(errorCode == TOO_MANY_ATTEMPTS_ERROR_CODE) {
                    setSubtitleText(mStringProvider.getString(
                        R.string.error_fingerprint_too_many_failed_attempts_template,
                        mStringProvider.getString(
                        if(mMode == Mode.SETUP) {
                            R.string.fingerprint_dialog_setup_attempts_used_up_message
                        } else {
                            R.string.fingerprint_dialog_scan_attempts_used_up_message
                        }
                    )))
                    showSubtitle()
                    displayErrorAndFinish(Error.TOO_MANY_ATTEMPTS, errString, ERROR_WITH_DETAILS_DELAY_IN_MILLIS)
                } else {
                    displayErrorAndFinish(Error.UNDEFINED_ERROR, errString, ERROR_WITHOUT_DETAILS_DELAY_IN_MILLIS)
                }

                mIsErrorPending = true
            }


            override fun onAuthenticationHelp(helpCode: Int, helpString: CharSequence) {
                displayError(helpString)
            }


            override fun onAuthenticationFailed() {
                displayError(mStringProvider.getString(R.string.error_fingerprint_not_recognized))
            }


            override fun onAuthenticationSucceeded(result: FingerprintManagerCompat.AuthenticationResult) {
                removeRunnable(mResetErrorRunnable)

                iconIv.setImageDrawable(mSuccessDrawable)

                statusTv.setTextColor(mSuccessColor)
                statusTv.text = mStringProvider.getString(R.string.fingerprint_dialog_success_status_text)

                postDelayed(
                    mSuccessRunnable,
                    SUCCESS_DELAY_IN_MILLIS
                )

                mIsSuccessPending = true
            }

        }


    private val mResetErrorRunnable: Runnable = Runnable {
        statusTv.setTextColor(mStatusTextColor)
        statusTv.text = mStringProvider.getString(R.string.fingerprint_dialog_default_status_text)

        iconIv.setImageResource(R.mipmap.ic_fingerprint)
    }


    private val mSuccessRunnable: Runnable = Runnable {
        onSuccess()
    }


    private val mErrorRunnable: Runnable = Runnable {
        onError(mLastFatalError!!)
    }


    enum class Mode {

        SETUP,
        SCAN

    }


    enum class Error {

        FAILED_TO_INITIALIZE_FINGERPRINT_SCANNER,
        TOO_MANY_ATTEMPTS,
        UNDEFINED_ERROR

    }


    /**
     * A listener to use to get notified of successes
     * and failures when authenticating.
     */
    interface Listener {

        fun onSuccess() {
            // Stub
        }

        fun onError(error: Error) {
            // Stub
        }

        fun onDismiss() {
            // Stub
        }

    }


}