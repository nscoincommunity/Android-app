package com.stocksexchange.android.ui.base.activities

import androidx.annotation.CallSuper
import com.stocksexchange.android.R
import com.stocksexchange.android.ui.base.fragments.BaseFragment
import com.stocksexchange.android.ui.base.mvp.presenters.BasePresenter

/**
 * A base activity that hosts a single fragment.
 */
abstract class BaseFragmentActivity<F : BaseFragment<*>, P : BasePresenter<*, *>> : BaseActivity<P>() {


    protected lateinit var mFragment: F




    @CallSuper
    override fun init() {
        super.init()

        initFragment()
    }


    @Suppress("UNCHECKED_CAST")
    protected open fun initFragment() {
        val foundFragment = supportFragmentManager.findFragmentById(R.id.fragmentHolderFl)

        mFragment = if(foundFragment == null) {
            getActivityFragment()
        } else {
            (foundFragment as F)
        }

        mFragment.onSelected()

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentHolderFl, mFragment)
            .commit()
    }


    protected abstract fun getActivityFragment(): F


}