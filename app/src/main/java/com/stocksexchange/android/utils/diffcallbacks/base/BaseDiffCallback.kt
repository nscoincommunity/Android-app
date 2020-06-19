package com.stocksexchange.android.utils.diffcallbacks.base

import androidx.recyclerview.widget.DiffUtil
import com.arthurivanets.adapster.model.BaseItem

abstract class BaseDiffCallback<M, out IT : BaseItem<M, *, *>>(
    protected val oldList: List<IT>,
    protected val newList: List<IT>
) : DiffUtil.Callback() {


    override fun getOldListSize(): Int = oldList.size


    override fun getNewListSize(): Int = newList.size


    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return (oldList[oldItemPosition].getItemModel() == newList[newItemPosition].getItemModel())
    }


    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return areItemsTheSame(oldList[oldItemPosition].getItemModel(), newList[newItemPosition].getItemModel())
    }


    protected abstract fun areItemsTheSame(oldItem: M, newItem: M): Boolean


}