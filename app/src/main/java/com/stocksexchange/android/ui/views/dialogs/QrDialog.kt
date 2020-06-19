package com.stocksexchange.android.ui.views.dialogs

import android.content.Context
import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import com.stocksexchange.android.R
import com.stocksexchange.android.ui.views.dialogs.base.BaseMaterialDialog
import com.stocksexchange.android.utils.extensions.getPrimaryButtonBackground

/**
 * A dialog used for showing QR-related information.
 */
class QrDialog(context: Context) : BaseMaterialDialog(context) {


    private lateinit var mTitleTv: TextView
    private lateinit var mQrCodeTextTv: TextView

    private lateinit var mCopyQrCodeTextBtnTv: TextView
    private lateinit var mSaveQrCodeImageBtnTv: TextView

    private lateinit var mQrCodeImageIv: ImageView

    private lateinit var mContentContainerLl: LinearLayout




    override fun initViews() {
        mTitleTv = findViewById(R.id.titleTv)
        mQrCodeTextTv = findViewById(R.id.qrCodeTextTv)

        mCopyQrCodeTextBtnTv = findViewById(R.id.copyQrCodeTextBtnTv)
        mCopyQrCodeTextBtnTv.text = mStringProvider.getString(
            R.string.qr_dialog_copy_qr_code_text_btn_text
        )

        mSaveQrCodeImageBtnTv = findViewById(R.id.saveQrCodeImageBtnTv)
        mSaveQrCodeImageBtnTv.text = mStringProvider.getString(
            R.string.qr_dialog_save_qr_code_image_btn_text
        )

        mQrCodeImageIv = findViewById(R.id.qrCodeImageIv)
        mContentContainerLl = findViewById(R.id.contentContainerLl)
    }


    fun setBackgroundColor(@ColorInt color: Int) {
        mContentContainerLl.setBackgroundColor(color)
    }


    fun setTitleTextColor(@ColorInt color: Int) {
        mTitleTv.setTextColor(color)
    }


    fun setQrCodeTextColor(@ColorInt color: Int) {
        mQrCodeTextTv.setTextColor(color)
    }


    fun setButtonsBackgroundColor(@ColorInt pressedStateBackgroundColor: Int,
                                  @ColorInt releasedStateBackgroundColor: Int) {
        val context = mCopyQrCodeTextBtnTv.context

        mCopyQrCodeTextBtnTv.background = context.getPrimaryButtonBackground(
            pressedStateBackgroundColor,
            releasedStateBackgroundColor
        )
        mSaveQrCodeImageBtnTv.background = context.getPrimaryButtonBackground(
            pressedStateBackgroundColor,
            releasedStateBackgroundColor
        )
    }


    fun setTitleText(text: String) {
        mTitleTv.text = text
    }


    fun setQrCodeText(text: String) {
        mQrCodeTextTv.text = text
    }


    fun setQrCodeImage(qrCodeImage: Bitmap) {
        mQrCodeImageIv.setImageBitmap(qrCodeImage)
    }


    fun setCopyQrCodeTextButtonClickListener(listener: ((View) -> Unit)) {
        mCopyQrCodeTextBtnTv.setOnClickListener {
            listener.invoke(it)
        }
    }


    fun setSaveQrCodeImageButtonClickListener(listener: ((View) -> Unit)) {
        mSaveQrCodeImageBtnTv.setOnClickListener {
            listener.invoke(it)
        }
    }


    override fun getLayoutResourceId(): Int = R.layout.qr_dialog_layout


    fun getTitleText(): String = mTitleTv.text.toString()


    fun getQrCodeText(): String = mQrCodeTextTv.text.toString()


}