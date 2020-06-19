package com.stocksexchange.android.ui.verification.prompt

import com.stocksexchange.android.BuildConfig
import com.stocksexchange.android.Constants
import com.stocksexchange.android.R
import com.stocksexchange.android.model.VerificationPromptDescriptionType
import com.stocksexchange.android.ui.base.mvp.model.StubModel
import com.stocksexchange.android.ui.base.mvp.presenters.BasePresenter
import com.stocksexchange.android.utils.SavedState
import com.stocksexchange.android.utils.extensions.extract

class VerificationPromptPresenter(
    view: VerificationPromptContract.View,
    model: StubModel
) : BasePresenter<VerificationPromptContract.View, StubModel>(view, model),
    VerificationPromptContract.ActionListener {


    var descriptionType = VerificationPromptDescriptionType.LONG




    override fun onEuParliamentDirectiveClicked() {
        mView.launchBrowser(Constants.EU_PARLIAMENT_DIRECTIVE_LINK_URL)
    }


    override fun onVerifyNowButtonClicked() {
        if(BuildConfig.IS_CHINESE_FLAVOR) {
            showInfoDialog(content = mStringProvider.getString(
                R.string.verification_prompt_not_yet_implemented_dialog_content
            ))
        } else {
            mView.launchBrowser(Constants.STEX_PROFILE_VERIFICATION_URL)
            mView.finishActivity()
        }
    }


    override fun onVerifyLaterButtonClicked() {
        mView.finishActivity()
    }


    override fun onSwipedToBottom() {
        mView.finishActivity()
    }


    override fun onRestoreState(savedState: SavedState) {
        super.onRestoreState(savedState)

        savedState.extract(presenterStateExtractor).also {
            descriptionType = it.descriptionType
        }
    }


    override fun onSaveState(savedState: SavedState) {
        super.onSaveState(savedState)

        savedState.saveState(PresenterState(
            descriptionType = descriptionType
        ))
    }


}