package com.stocksexchange.core.handlers

import android.graphics.Bitmap
import com.google.zxing.EncodeHintType
import net.glxn.qrgen.android.QRCode
import net.glxn.qrgen.core.image.ImageType
import java.io.File

class QrCodeHandler {


    companion object {

        private const val QR_CODE_IMAGE_DEFAULT_MARGIN = 1

    }




    fun generateQrCodeImage(text: String, size: Int): Bitmap {
        return QRCode.from(text)
            .withSize(size, size)
            .withHint(EncodeHintType.MARGIN, QR_CODE_IMAGE_DEFAULT_MARGIN)
            .bitmap()
    }


    fun generateQrCodeImageAndSaveToFile(text: String, size: Int,
                                         file: File) {
        QRCode.from(text)
            .withSize(size, size)
            .withHint(EncodeHintType.MARGIN, QR_CODE_IMAGE_DEFAULT_MARGIN)
            .to(ImageType.JPG)
            .writeTo(file.outputStream())
    }


}