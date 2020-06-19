package com.stocksexchange.android.model

import com.stocksexchange.android.R
import com.stocksexchange.android.utils.providers.StringProvider

/**
 * A helper builder class containing all the properties
 * and values for building a MaterialDialog instance.
 */
class MaterialDialogBuilder private constructor(
    val title: String = "",
    val content: String = "",
    val items: Array<String> = arrayOf(),
    val selectedItemIndex: Int = -1,
    val negativeBtnText: String = "",
    val positiveBtnText: String = "",
    val isCancelable: Boolean = true,
    val itemsCallback: ((String) -> Unit)? = null,
    val itemsCallbackSingleChoice: ((Int) -> Unit)? = null,
    val negativeBtnClick: (() -> Unit)? = null,
    val positiveBtnClick: (() -> Unit)? = null
) {


    companion object {

        fun confirmationDialog(
            title: String = "",
            content: String = "",
            negativeBtnText: String = "",
            positiveBtnText: String = "",
            isCancelable: Boolean = true,
            negativeBtnClick: (() -> Unit)? = null,
            positiveBtnClick: (() -> Unit)? = null
        ): MaterialDialogBuilder {
            return MaterialDialogBuilder(
                title = title,
                content = content,
                negativeBtnText = negativeBtnText,
                positiveBtnText = positiveBtnText,
                isCancelable = isCancelable,
                negativeBtnClick = negativeBtnClick,
                positiveBtnClick = positiveBtnClick
            )
        }


        fun infoDialog(title: String, content: String,
                       stringProvider: StringProvider): MaterialDialogBuilder {
            return MaterialDialogBuilder(
                title = title,
                content = content,
                positiveBtnText = stringProvider.getString(R.string.ok)
            )
        }


        fun errorDialog(content: String,
                        stringProvider: StringProvider): MaterialDialogBuilder {
            return infoDialog(
                title = stringProvider.getString(R.string.error),
                content = content,
                stringProvider = stringProvider
            )
        }


        fun listDialog(
            title: String = "",
            items: Array<String>,
            selectedItemIndex: Int = -1,
            negativeBtnText: String = "",
            positiveBtnText: String = "",
            itemsCallback: ((String) -> Unit)? = null,
            itemsCallbackSingleChoice: ((Int) -> Unit)? = null
        ): MaterialDialogBuilder {
            return MaterialDialogBuilder(
                title = title,
                items = items,
                selectedItemIndex = selectedItemIndex,
                negativeBtnText = negativeBtnText,
                positiveBtnText = positiveBtnText,
                itemsCallback = itemsCallback,
                itemsCallbackSingleChoice = itemsCallbackSingleChoice
            )
        }

    }




    val hasTitle: Boolean
        get() = title.isNotBlank()


    val hasContent: Boolean
        get() = content.isNotBlank()


    val hasItems: Boolean
        get() = items.isNotEmpty()


    val hasSelectedItemIndex: Boolean
        get() = (selectedItemIndex != -1)


    val hasNegativeBtnText: Boolean
        get() = negativeBtnText.isNotBlank()


    val hasPositiveBtnText: Boolean
        get() = positiveBtnText.isNotBlank()


    val hasItemsCallback: Boolean
        get() = (itemsCallback != null)


    val hasItemsCallbackSingleChoice: Boolean
        get() = (itemsCallbackSingleChoice != null)


}