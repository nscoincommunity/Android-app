package com.stocksexchange.core.providers

import android.content.Context
import com.stocksexchange.core.utils.extensions.getFingerprintManager

class FingerprintProvider(private val context: Context) {


    /**
     * Checks whether the device has a fingerprint.
     *
     * @return true if present; false otherwise
     */
    fun isHardwareAvailable(): Boolean {
        return context.getFingerprintManager().isHardwareDetected
    }


    /**
     * Checks whether there is at least one fingerprint enrolled.
     *
     * @return true if enrolled; false otherwise
     */
    fun hasEnrolledFingerprints(): Boolean {
        return context.getFingerprintManager().hasEnrolledFingerprints()
    }


}