package com.stocksexchange.android.ui.base.viewpager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

/**
 * A base adapter for ViewPager widgets.
 */
abstract class BaseViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {


    /**
     * The view pager id that this adapter is associated with.
     * Used for calculating tags associated with fragments.
     */
    var viewPagerId: Int = -1

    protected val mFragmentManager: FragmentManager = fm

    protected val mFragmentList: MutableList<Fragment> = mutableListOf()

    /**
     * Retrieves a list of the added fragments.
     */
    val fragments: List<Fragment>
        get() = mFragmentList




    /**
     * Performs a specific action on each fragment.
     *
     * @param action The action to perform
     */
    fun forEachFragment(action: ((Fragment) -> Unit)) {
        mFragmentList.forEach(action)
    }


    fun addFragment(fragment: Fragment) {
        mFragmentList.add(fragment)
    }


    /**
     * Denotes whether this adapter is empty or not.
     *
     * @return true if empty; false otherwise
     */
    fun isEmpty(): Boolean = (count == 0)


    /**
     * Retrieves a tag associated with a fragment at the specified position.
     *
     * @param fragmentPosition The position of a specific fragment within this adapter
     *
     * @return Unique tag for the fragment at the specified position
     */
    fun getTagForFragmentPosition(fragmentPosition: Int): String {
        return "android:switcher:$viewPagerId:$fragmentPosition"
    }


    /**
     * Attempts to find and return a fragment associated with a tag.
     *
     * @param tag The tag associated with the fragment
     *
     * @return The fragment associated with the tag or null if the tag is not associated
     * with any fragment
     */
    fun getFragmentForTag(tag: String): Fragment? {
        return mFragmentManager.findFragmentByTag(tag)
    }


    /**
     * Tries to find and return a fragment at the specified position.
     *
     * @param position The position of the fragment within this adapter
     *
     * @return The fragment at the specified position or null if there is
     * not a fragment at the specified position
     */
    fun getFragment(position: Int): Fragment? {
        return getFragmentForTag(getTagForFragmentPosition(position))
    }


    override fun getCount() = mFragmentList.size


    override fun getItem(position: Int): Fragment {
        return position.takeIf { it in 0 until count }.let { mFragmentList[position] }
    }


}