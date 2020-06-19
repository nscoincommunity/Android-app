package com.stocksexchange.android.ui.verification.prompt

import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.style.UnderlineSpan
import android.view.MotionEvent
import android.view.View
import androidx.core.text.toSpannable
import androidx.core.view.GestureDetectorCompat
import com.stocksexchange.android.R
import com.stocksexchange.android.model.TransitionAnimations
import com.stocksexchange.android.model.VerificationPromptDescriptionType
import com.stocksexchange.android.theming.ThemingUtil
import com.stocksexchange.android.ui.base.activities.BaseActivity
import com.stocksexchange.android.utils.handlers.BrowserHandler
import com.stocksexchange.core.utils.extensions.extract
import com.stocksexchange.core.utils.extensions.set
import com.stocksexchange.core.utils.listeners.GestureListener
import com.stocksexchange.core.utils.text.CustomLinkMovementMethod
import com.stocksexchange.core.utils.text.SelectorSpan
import kotlinx.android.synthetic.main.verification_prompt_activity_layout.*
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class VerificationPromptActivity : BaseActivity<VerificationPromptPresenter>(),
    VerificationPromptContract.View {


    companion object {}


    override val mPresenter: VerificationPromptPresenter by inject { parametersOf(this) }

    private lateinit var mGestureDetector: GestureDetectorCompat




    override fun onFetchExtras(extras: Bundle) {
        extras.extract(extrasExtractor).also {
            mPresenter.descriptionType = it.descriptionType
        }
    }


    override fun preInit() {
        super.preInit()

        mGestureDetector = GestureDetectorCompat(
            this,
            GestureListener(mGestureDetectorListener)
        )
    }


    override fun init() {
        super.init()

        initContentContainer()
        initIcon()
        initTitle()
        initDescription()
        initButtons()
    }


    private fun initContentContainer() {
        ThemingUtil.VerificationPrompt.contentContainer(contentContainerRl, getAppTheme())
    }


    private fun initIcon() {
        with(iconIv) {
            setImageResource(R.mipmap.ic_verification_prompt)
        }
    }


    private fun initTitle() {
        with(titleTv) {
            text = getStr(R.string.verification_prompt_title_text)

            ThemingUtil.VerificationPrompt.title(this, getAppTheme())
        }
    }


    private fun initDescription() {
        with(descriptionTv) {
            highlightColor = Color.TRANSPARENT
            movementMethod = CustomLinkMovementMethod()
            text = getDescriptionSpannable()

            ThemingUtil.VerificationPrompt.description(this, getAppTheme())
        }
    }


    private fun initButtons() {
        initVerifyNowButton()
        initVerifyLaterButton()
    }


    private fun initVerifyNowButton() {
        with(verifyNowBtn) {
            text = getStr(R.string.action_verify_now)

            setOnClickListener {
                mPresenter.onVerifyNowButtonClicked()
            }

            ThemingUtil.VerificationPrompt.verifyNowButton(this, getAppTheme())
        }
    }


    private fun initVerifyLaterButton() {
        with(verifyLaterBtn) {
            text = getStr(R.string.action_verify_later)

            setOnClickListener {
                mPresenter.onVerifyLaterButtonClicked()
            }

            ThemingUtil.VerificationPrompt.verifyLaterButton(this, getAppTheme())
        }
    }


    override fun launchBrowser(url: String) {
        get<BrowserHandler>().launchBrowser(this, url, getAppTheme())
    }


    private fun getDescriptionSpannable(): Spannable {
        return getLongDescriptionSpannable(getDescriptionRawString())
    }


    private fun getDescriptionRawString(): String {
        return when(mPresenter.descriptionType) {
            VerificationPromptDescriptionType.LONG -> {
                mStringProvider.getString(
                    R.string.verification_prompt_long_description_template,
                    getStr(R.string.verification_prompt_long_description_link_text)
                )
            }

            VerificationPromptDescriptionType.SHORT -> {
                getStr(R.string.verification_prompt_short_description)
            }
        }
    }


    private fun getLongDescriptionSpannable(rawString: String): Spannable {
        val linkText = getStr(R.string.verification_prompt_long_description_link_text)
        val selectorSpan = object : SelectorSpan(
            getAppTheme().generalTheme.primaryTextColor,
            getAppTheme().generalTheme.primaryTextColor
        ) {

            override fun onClick(widget: View) {
                mPresenter.onEuParliamentDirectiveClicked()
            }

            override fun shouldHighlightBackground(): Boolean = false

        }
        val underlineSpan = UnderlineSpan()

        return rawString.toSpannable().apply {
            val startIndex = rawString.indexOf(linkText)
            val endIndex = (startIndex + linkText.length)
            val spans = arrayOf(selectorSpan, underlineSpan)

            for(span in spans) {
                this[startIndex, endIndex] = span
            }
        }
    }


    private fun getShortDescriptionSpannable(rawString: String): Spannable {
        return rawString.toSpannable()
    }


    override fun getContentLayoutResourceId(): Int = R.layout.verification_prompt_activity_layout


    override fun getTransitionAnimations(): TransitionAnimations {
        return TransitionAnimations.VERTICAL_SLIDING_ANIMATIONS
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        mGestureDetector.onTouchEvent(event)

        return super.onTouchEvent(event)
    }


    private val mGestureDetectorListener = object : GestureListener.Listener {

        override fun onSwipedToBottom(startEvent: MotionEvent, endEvent: MotionEvent) {
            mPresenter.onSwipedToBottom()
        }

    }


}