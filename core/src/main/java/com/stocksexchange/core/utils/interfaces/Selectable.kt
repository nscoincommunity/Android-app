package com.stocksexchange.core.utils.interfaces

/**
 * An interface to implement to mark a class to be selectable.
 */
interface Selectable {


    /**
     * Selects/deselects a particular item.
     *
     * @param isSelected Whether the item is selected or not
     * @param source The source of the selection
     */
    fun setSelected(isSelected: Boolean, source: Source)


    /**
     * Returns whether the item is currently selected or not.
     *
     * @return true if selected; false otherwise
     */
    fun isSelected(): Boolean


    /**
     * This method is called to notify you that the
     * object has been selected.
     */
    fun onSelected()


    /**
     * This method is called to notify you that the
     * object has been unselected.
     */
    fun onUnselected()


    enum class Source(val delay: Long) {

        TAB(delay = 400L),  // TabLayout
        SWIPE(delay = 400L), // ViewPager
        MENU(delay = 0L),   // BottomNavigationView
        CHILD(delay = 0L)   // Child Fragment

    }


}