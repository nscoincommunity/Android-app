package com.stocksexchange.android.ui.settings

import android.content.Context
import android.view.View
import android.widget.CompoundButton
import com.arthurivanets.adapster.model.BaseItem
import com.arthurivanets.adapster.recyclerview.TrackableRecyclerViewAdapter
import com.stocksexchange.android.model.Setting
import com.stocksexchange.android.ui.settings.items.SettingAccountItem
import com.stocksexchange.android.ui.settings.items.SettingItem

class SettingsRecyclerViewAdapter(
    context: Context,
    items: MutableList<BaseItem<*, *, *>>
) : TrackableRecyclerViewAdapter<Int, BaseItem<*, *, *>, BaseItem.ViewHolder<*>>(context, items) {


    private lateinit var mResources: SettingResources

    var onSettingItemClickListener: ((View, SettingItem, Int) -> Unit)? = null
    var onSwitchClickListener: ((CompoundButton, SettingItem, Int, Boolean) -> Unit)? = null
    var onSignOutClickListener: ((View, SettingAccountItem, Int) -> Unit)? = null




    override fun assignListeners(holder: BaseItem.ViewHolder<*>, position: Int, item: BaseItem<*, *, *>) {
        super.assignListeners(holder, position, item)

        when(item.getLayout()) {

            SettingItem.MAIN_LAYOUT_ID ->  {
                val settingItem = (item as SettingItem)
                val settingViewHolder = (holder as SettingItem.ViewHolder)

                with(settingItem) {
                    setOnItemClickListener(settingViewHolder, position, onSettingItemClickListener)
                    setOnSwitchClickListener(settingViewHolder, position, onSwitchClickListener)
                }
            }

            SettingAccountItem.MAIN_LAYOUT_ID ->  {
                val settingAccountItem = (item as SettingAccountItem)
                val settingAccountViewHolder = (holder as SettingAccountItem.ViewHolder)

                with(settingAccountItem) {
                    setOnSignOutClickListener(settingAccountViewHolder, position, onSignOutClickListener)
                }
            }

        }
    }


    fun setResources(resources: SettingResources) {
        mResources = resources
    }


    /**
     * Retrieves a data set position for a particular setting.
     *
     * @param setting The setting to get a data set position for
     *
     * @return The data set position
     */
    fun getDataSetPositionForSetting(setting: Setting): Int {
        for(index in items.indices) {
            if(items[index] is SettingItem && (items[index] as SettingItem).itemModel.id == setting.id) {
                return index
            }
        }

        return itemCount
    }


    override fun getResources(): SettingResources? {
        return mResources
    }


}