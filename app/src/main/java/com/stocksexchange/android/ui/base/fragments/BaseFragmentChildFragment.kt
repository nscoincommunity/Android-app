package com.stocksexchange.android.ui.base.fragments

import androidx.annotation.CallSuper
import com.stocksexchange.android.R
import com.stocksexchange.android.ui.base.mvp.presenters.BasePresenter
import com.stocksexchange.core.utils.interfaces.Selectable

/**
 * A base fragment that hosts a single fragment.
 */
abstract class BaseFragmentChildFragment<F : BaseFragment<*>, P : BasePresenter<*, *>> : BaseFragment<P>() {


    protected lateinit var mFragment: F




    @CallSuper
    override fun init() {
        super.init()

        initFragment()
    }


    @Suppress("UNCHECKED_CAST")
    protected open fun initFragment() {
        val foundFragment = childFragmentManager.findFragmentById(R.id.fragmentHolderFl)

        mFragment = if(foundFragment == null) {
            getActivityFragment()
        } else {
            (foundFragment as F)
        }

        mFragment.setSelected(true, Selectable.Source.CHILD)

        childFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentHolderFl, mFragment)
            .commit()
    }


    protected abstract fun getActivityFragment(): F


}