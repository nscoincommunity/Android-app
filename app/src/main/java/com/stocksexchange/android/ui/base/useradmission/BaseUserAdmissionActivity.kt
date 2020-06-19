package com.stocksexchange.android.ui.base.useradmission

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.CallSuper
import com.stocksexchange.android.R
import com.stocksexchange.android.model.SystemWindowType
import com.stocksexchange.android.model.TransitionAnimations
import com.stocksexchange.android.model.UserAdmissionButtonType
import com.stocksexchange.android.theming.ThemingUtil
import com.stocksexchange.android.ui.base.activities.BaseActivity
import com.stocksexchange.android.ui.views.UserAdmissionButton
import com.stocksexchange.core.utils.extensions.makeGone
import com.stocksexchange.core.utils.extensions.makeVisible
import com.stocksexchange.core.utils.helpers.crossFade
import com.stocksexchange.core.utils.interfaces.Clearable

/**
 * A base activity that contains common functionality among
 * all user admission screens (login, registration, password
 * recovery, etc.).
 */
abstract class BaseUserAdmissionActivity<P, PP, IV> : BaseActivity<P>(),
    UserAdmissionView<PP, IV> where
        P : BaseUserAdmissionPresenter<*, *, *, PP, IV>,
        PP : Enum<*>,
        IV : Enum<*> {


    companion object {

        private const val ANIMATION_DURATION = 300L

        private val ANIMATION_INTERPOLATOR = LinearInterpolator()

    }


    protected var mSelectedMainView: View? = null

    private lateinit var mMainViews: Array<View>




    override fun init() {
        super.init()

        initContentContainer()
        initTitle()
        initMotto()
        initSelectedMainView()
        initMainViews()
        initButtons()
    }


    protected open fun initContentContainer() {
        ThemingUtil.UserAdmission.contentContainer(getContentView(), getAppTheme())
    }


    protected open fun initTitle() {
        ThemingUtil.UserAdmission.title(getAppTitleIv(), getAppTheme())
    }


    protected open fun initMotto() {
        with(getAppMottoTv()) {
            text = getStr(R.string.app_motto)

            ThemingUtil.UserAdmission.motto(this, getAppTheme())
        }
    }


    @CallSuper
    protected open fun initSelectedMainView() {
        mSelectedMainView = getCurrentlyVisibleMainView(mPresenter.processPhase)
        mSelectedMainView?.makeVisible()
    }


    @CallSuper
    protected open fun initMainViews() {
        mMainViews = getMainViewsArray()
        mMainViews.forEach {
            if(it.id != mSelectedMainView?.id) {
                it.makeGone()
            }
        }
    }


    protected open fun initButtons() {
        val hasSecondaryButton = hasSecondaryButton()

        val primaryButton = getPrimaryButton()
        val secondaryButton = getSecondaryButton()

        with(primaryButton) {
            setPrimaryButtonText(getPrimaryButtonText())
            setOnButtonClickListener {
                mPresenter.onPrimaryButtonClicked()
            }
        }

        if(hasSecondaryButton) {
            with(secondaryButton!!) {
                visibility = getSecondaryButtonVisibility()

                setSecondaryButtonText(getSecondaryButtonText())
                setOnButtonClickListener {
                    mPresenter.onSecondaryButtonClicked()
                }
            }
        }

        with(ThemingUtil.UserAdmission) {
            val theme = getAppTheme()

            button(primaryButton, theme)

            if(hasSecondaryButton) {
                secondaryButton(secondaryButton!!, theme)
            }
        }
    }


    override fun showButtonProgressBar(type: UserAdmissionButtonType) {
        if((type == UserAdmissionButtonType.SECONDARY) && !hasSecondaryButton()) {
            return
        }

        when(type) {
            UserAdmissionButtonType.PRIMARY -> getPrimaryButton().showProgressBar()
            UserAdmissionButtonType.SECONDARY -> getSecondaryButton()?.showProgressBar()
        }
    }


    override fun hideButtonProgressBar(type: UserAdmissionButtonType) {
        if((type == UserAdmissionButtonType.SECONDARY) && !hasSecondaryButton()) {
            return
        }

        when(type) {
            UserAdmissionButtonType.PRIMARY -> getPrimaryButton().hideProgressBar()
            UserAdmissionButtonType.SECONDARY -> getSecondaryButton()?.hideProgressBar()
        }
    }


    override fun showSecondaryButton() {
        if(!hasSecondaryButton()) {
            return
        }

        with(getSecondaryButton()!!) {
            alpha = 0f
            makeVisible()

            animate()
                .alpha(1f)
                .setDuration(ANIMATION_DURATION)
                .setInterpolator(ANIMATION_INTERPOLATOR)
                .start()
        }
    }


    override fun hideSecondaryButton() {
        if(!hasSecondaryButton()) {
            return
        }

        with(getSecondaryButton()!!) {
            animate()
                .alpha(0f)
                .setDuration(ANIMATION_DURATION)
                .setListener(object : AnimatorListenerAdapter() {

                    override fun onAnimationEnd(animation: Animator?) {
                        makeGone()
                    }

                })
                .setInterpolator(ANIMATION_INTERPOLATOR)
                .start()
        }
    }


    override fun updateMainView(animate: Boolean) {
        if(mSelectedMainView == null) {
            return
        }

        val viewToHide = mSelectedMainView!!
        val viewToShow = getCurrentlyVisibleMainView(mPresenter.processPhase)

        if((viewToShow == null) || (viewToHide.id == viewToShow.id)) {
            return
        }

        if(viewToShow is Clearable) {
            viewToShow.clear()
        }

        mSelectedMainView = viewToShow

        if(animate) {
            crossFade(
                viewToHide = viewToHide,
                viewToShow = viewToShow,
                duration = ANIMATION_DURATION,
                interpolator = ANIMATION_INTERPOLATOR,
                onBeforeShowingView = {
                    viewToHide.makeGone()
                    viewToHide.alpha = 1f

                    viewToShow.alpha = 0f
                    viewToShow.makeVisible()

                    mPresenter.onBeforeShowingNewMainView()
                },
                onFinish = {
                    mPresenter.onNewMainViewShowingFinished()
                }
            )
        } else {
            viewToHide.makeGone()
            mPresenter.onBeforeShowingNewMainView()
            viewToShow.makeVisible()
            mPresenter.onNewMainViewShowingFinished()
        }
    }


    override fun setPrimaryButtonText(text: String) {
        getPrimaryButton().setButtonText(text)
    }


    override fun setSecondaryButtonText(text: String) {
        if(hasSecondaryButton()) {
            getSecondaryButton()?.setButtonText(text)
        }
    }


    override fun shouldSetColorForSystemWindow(type: SystemWindowType): Boolean {
        return when(type) {
            SystemWindowType.STATUS_BAR,
            SystemWindowType.NAVIGATION_BAR -> false

            else -> super.shouldSetColorForSystemWindow(type)
        }
    }


    protected abstract fun hasSecondaryButton(): Boolean


    override fun shouldShowIntercomInAppMessagePopups(): Boolean = false


    protected open fun getSecondaryButtonVisibility(): Int = -1


    protected abstract fun getPrimaryButtonText(): String


    protected open fun getSecondaryButtonText(): String = ""


    abstract fun getInitialProcessPhase(): PP


    override fun getTransitionAnimations(): TransitionAnimations {
        return TransitionAnimations.FADING_ANIMATIONS
    }


    protected abstract fun getCurrentlyVisibleMainView(phase: PP): View?


    protected abstract fun getAppTitleIv(): ImageView


    protected abstract fun getAppMottoTv(): TextView


    protected abstract fun getPrimaryButton(): UserAdmissionButton


    protected open fun getSecondaryButton(): UserAdmissionButton? = null


    protected abstract fun getContentView(): View


    protected abstract fun getMainViewsArray(): Array<View>


}