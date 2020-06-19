package com.stocksexchange.android.ui.settings.items

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.arthurivanets.adapster.Adapter
import com.arthurivanets.adapster.markers.ItemResources
import com.arthurivanets.adapster.model.BaseItem
import com.arthurivanets.adapster.model.Item
import com.stocksexchange.android.R
import com.stocksexchange.android.model.Setting
import com.stocksexchange.android.model.SettingId
import com.stocksexchange.android.ui.base.viewholders.BaseViewHolder
import com.stocksexchange.android.ui.settings.SettingResources
import com.stocksexchange.android.theming.ThemingUtil
import com.stocksexchange.android.theming.model.Theme
import com.stocksexchange.core.utils.extensions.*

class SettingItem(itemModel: Setting) : BaseItem<Setting, SettingItem.ViewHolder, SettingResources>(itemModel) {


    companion object {

        const val MAIN_LAYOUT_ID = R.layout.setting_item_layout

    }




    override fun init(adapter: Adapter<out Item<RecyclerView.ViewHolder, ItemResources>>?, parent: ViewGroup,
                      inflater: LayoutInflater, resources: SettingResources?): ViewHolder {
        return ViewHolder(inflater.inflate(layout, parent, false))
    }


    override fun bind(adapter: Adapter<out Item<RecyclerView.ViewHolder, ItemResources>>?,
                      viewHolder: ViewHolder, resources: SettingResources?) {
        super.bind(adapter, viewHolder, resources)

        with(viewHolder) {
            mTitleTv.text = itemModel.title

            if(itemModel.hasDescription) {
                mDescriptionTv.text = itemModel.description
                mDescriptionTv.makeVisible()
            } else {
                mDescriptionTv.makeGone()
            }

            mSwitchView.setOnCheckedChangeListener(null)

            if(itemModel.isCheckable) {
                mSwitchView.isChecked = itemModel.isChecked

                if(itemModel.id == SettingId.FINGERPRINT_UNLOCK) {
                    mSwitchView.isEnabled = false
                    mSwitchView.isClickable = false
                    mSwitchView.isFocusable = false
                } else {
                    mSwitchView.isEnabled = true
                    mSwitchView.isClickable = true
                    mSwitchView.isFocusable = true
                }

                mSwitchView.makeVisible()
                mIconIv.makeGone()
            } else {
                mSwitchView.makeGone()
                mIconIv.makeVisible()
            }

            if(itemModel.isEnabled) {
                itemView.enable(changeAlpha = true, childrenToo = true)
            } else {
                itemView.disable(changeAlpha = true, childrenToo = true)
            }

            applyTheme(resources!!.settings.theme)
        }
    }


    fun setOnItemClickListener(viewHolder: ViewHolder, position: Int,
                               listener: ((View, SettingItem, Int) -> Unit)?) {
        with(viewHolder) {
            itemView.setOnClickListener {
                if(mSwitchView.isVisible && mSwitchView.isEnabled) {
                    mSwitchView.isChecked = !mSwitchView.isChecked
                } else {
                    listener?.invoke(it, this@SettingItem, position)
                }
            }
        }
    }


    fun setOnSwitchClickListener(viewHolder: ViewHolder, position: Int,
                                 listener: ((CompoundButton, SettingItem, Int, Boolean) -> Unit)?) {
        viewHolder.mSwitchView.setOnCheckedChangeListener { buttonView, isChecked ->
            listener?.invoke(buttonView, this@SettingItem, position, isChecked)
        }
    }


    override fun getLayout(): Int = MAIN_LAYOUT_ID


    class ViewHolder(itemView: View) : BaseViewHolder<Setting>(itemView) {

        val mTitleTv: TextView = itemView.findViewById(R.id.titleTv)
        val mDescriptionTv: TextView = itemView.findViewById(R.id.descriptionTv)

        val mSwitchView: SwitchCompat = itemView.findViewById(R.id.switchView)

        val mIconIv: ImageView = itemView.findViewById(R.id.iconIv)
        val mContentContainerRl: RelativeLayout = itemView.findViewById(R.id.contentContainerRl)


        override fun applyTheme(theme: Theme) {
            with(ThemingUtil.Settings.SettingItem) {
                if (boundData.customDescriptionColor) {
                    customDescriptionColor(mDescriptionTv, theme.generalTheme)
                } else {
                    description(mDescriptionTv, theme)
                }

                contentContainer(mContentContainerRl, theme)
                title(mTitleTv, theme)
                switch(mSwitchView, theme)
            }
        }

    }


}