package com.stocksexchange.core.utils.listeners.adapters

import android.widget.SeekBar

interface OnSeekBarChangeListenerAdapter : SeekBar.OnSeekBarChangeListener {


    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        // Stub
    }


    override fun onStartTrackingTouch(seekBar: SeekBar) {
        // Stub
    }


    override fun onStopTrackingTouch(seekBar: SeekBar) {
        // Stub
    }


}