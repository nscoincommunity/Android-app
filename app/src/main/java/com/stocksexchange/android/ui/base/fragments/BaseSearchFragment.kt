package com.stocksexchange.android.ui.base.fragments

import android.os.Bundle
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
import android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import com.stocksexchange.android.theming.ThemingUtil
import com.stocksexchange.android.ui.base.mvp.presenters.BasePresenter
import com.stocksexchange.android.ui.views.toolbars.SearchToolbar
import com.stocksexchange.core.utils.extensions.setSoftInputMode
import com.stocksexchange.core.utils.listeners.adapters.TextWatcherAdapter

/**
 * A base fragment that performs a search.
 */
abstract class BaseSearchFragment<T : BaseFragment<*>, P : BasePresenter<*, *>> : BaseFragmentChildFragment<T, P>() {


    companion object {

        const val KEY_SEARCH_QUERY = "search_query"

    }


    private var mIsSearchQueryEmpty: Boolean = true

    private var mSearchQuery: String = ""




    override fun onFetchExtras(extras: Bundle) = with(extras) {
        mSearchQuery = getString(KEY_SEARCH_QUERY, "")
        mIsSearchQueryEmpty = mSearchQuery.isEmpty()
    }


    override fun preInit() {
        super.preInit()

        setSoftInputMode(SOFT_INPUT_ADJUST_PAN)
    }


    override fun init() {
        super.init()

        initSearchToolbar()
    }


    fun getSearchQuery(): String = mSearchQuery


    private fun initSearchToolbar() {
        with(getSearchToolbar()) {
            setOnLeftButtonClickListener {
                mPresenter.onNavigateUpPressed()
            }

            setHintText(getInputHint())
            setInputType(getInputType())
            addTextWatcher(mSearchQueryTextWatcher)
            setOnEditorActionListener(mOnEditorActionListener)

            if(mIsSearchQueryEmpty) {
                hideClearInputButton(false)
            } else {
                showClearInputButton(false)
            }

            setOnClearInputButtonClickListener(mOnClearInputBtnClickListener)

            ThemingUtil.Main.searchToolbar(this, getAppTheme())
        }
    }


    private fun showClearInputButton() {
        getSearchToolbar().showClearInputButton(true)
    }


    private fun hideClearInputButton() {
        getSearchToolbar().hideClearInputButton(true)
    }


    private fun showKeyboard(shouldDelay: Boolean) {
        getSearchToolbar().showKeyboard(shouldDelay)
    }


    private fun hideKeyboard() {
        getSearchToolbar().hideKeyboard()
    }


    protected abstract fun performSearch(query: String)


    protected abstract fun cancelSearch()


    private fun setSearchQuery(query: String) {
        getSearchToolbar().setText(query)
    }


    protected abstract fun getInputHint(): String


    protected open fun getInputType(): Int = InputType.TYPE_CLASS_TEXT


    protected abstract fun getSearchToolbar(): SearchToolbar


    override fun onResume() {
        super.onResume()

        if(mIsSearchQueryEmpty) {
            showKeyboard(true)
        } else {
            hideKeyboard()
            setSearchQuery(mSearchQuery)
        }
    }


    override fun onSelected() {
        super.onSelected()

        if(isInitialized() && mIsSearchQueryEmpty) {
            showKeyboard(false)
        }
    }


    private val mSearchQueryTextWatcher: TextWatcher = object : TextWatcherAdapter {

        override fun onTextChanged(text: CharSequence, start: Int, before: Int, count: Int) {
            if(text.isNotEmpty()) {
                if(mIsSearchQueryEmpty) {
                    mIsSearchQueryEmpty = false

                    getSearchToolbar().showClearInputButton(true)
                }
            } else {
                if(!mIsSearchQueryEmpty) {
                    mIsSearchQueryEmpty = true

                    getSearchToolbar().hideClearInputButton(true)
                }
            }

            mSearchQuery = text.toString()

            performSearch(mSearchQuery)
        }

    }


    private val mOnEditorActionListener: OnEditorActionListener = OnEditorActionListener { _, actionId, _ ->
        if(actionId == EditorInfo.IME_ACTION_SEARCH) {
            hideKeyboard()
        }

        true
    }


    private val mOnClearInputBtnClickListener: ((View) -> Unit) = {
        cancelSearch()
        setSearchQuery("")
        showKeyboard(false)
    }


    override fun navigateBack(): Boolean {
        hideKeyboard()

        return super.navigateBack()
    }


    override fun onRestoreState(savedState: Bundle) {
        super.onRestoreState(savedState)

        savedState.apply {
            mSearchQuery = getString(KEY_SEARCH_QUERY, "")
            mIsSearchQueryEmpty = mSearchQuery.isEmpty()
        }
    }


    override fun onSaveState(savedState: Bundle) {
        super.onSaveState(savedState)

        with(savedState) {
            putString(KEY_SEARCH_QUERY, mSearchQuery)
        }
    }


    override fun onRecycle() {
        super.onRecycle()

        setSoftInputMode(SOFT_INPUT_ADJUST_UNSPECIFIED)
        getSearchToolbar().recycle()
    }


}