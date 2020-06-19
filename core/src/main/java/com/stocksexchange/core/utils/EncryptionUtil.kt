package com.stocksexchange.core.utils

import com.tozny.crypto.android.AesCbcWithIntegrity

/**
 * A utility class used for encrypting and decrypting text.
 */
class EncryptionUtil(private val encryptionKey: String) {


    private var mSecretKeys: AesCbcWithIntegrity.SecretKeys? = null




    init {
        initSecretKeysIfNecessary()
    }


    private fun initSecretKeysIfNecessary() {
        if(mSecretKeys != null) {
            return
        }

        try {
            mSecretKeys = AesCbcWithIntegrity.keys(encryptionKey)
        } catch(exception: Exception) {
            exception.printStackTrace()
        }
    }


    /**
     * Encrypts the given text data and returns it.
     *
     * @param textData The text data to encrypt
     *
     * @return The encrypted text data
     */
    fun encrypt(textData: String): String {
        if(textData.isEmpty()) {
            return ""
        }

        initSecretKeysIfNecessary()

        return try {
            AesCbcWithIntegrity.encrypt(textData, mSecretKeys).toString()
        } catch(e: Exception) {
            ""
        }
    }


    /**
     * Decrypts the given encrypted text data and returns it.
     *
     * @param encryptedTextData The encrypted text data to decrypt
     *
     * @return The decrypted text data
     */
    fun decrypt(encryptedTextData: String): String {
        if(encryptedTextData.isEmpty()) {
            return ""
        }

        initSecretKeysIfNecessary()

        return try {
            AesCbcWithIntegrity.decryptString(
                AesCbcWithIntegrity.CipherTextIvMac(encryptedTextData),
                mSecretKeys
            )
        } catch(e: Exception) {
            ""
        }
    }


}