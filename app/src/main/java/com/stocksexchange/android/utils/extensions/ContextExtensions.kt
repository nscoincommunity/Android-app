package com.stocksexchange.android.utils.extensions

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.*
import androidx.annotation.*
import androidx.annotation.IntRange
import com.stocksexchange.android.R
import android.graphics.drawable.Drawable
import android.text.method.DigitsKeyListener
import android.text.method.KeyListener
import androidx.core.graphics.drawable.toDrawable
import com.afollestad.materialdialogs.MaterialDialog
import com.stocksexchange.android.model.*
import com.stocksexchange.android.theming.ThemingUtil
import com.stocksexchange.android.theming.model.Theme
import com.stocksexchange.core.formatters.NumberFormatter
import com.stocksexchange.core.utils.extensions.*
import java.lang.StringBuilder


fun Context.getSelectableItemBackgroundDrawable(): Drawable? {
    return getCompatDrawable(getResourceIdFromAttributes(
        attributes = intArrayOf(R.attr.selectableItemBackground),
        index = 0
    ))
}


fun Context.getSelectableItemBackgroundBorderlessDrawable(): Drawable? {
    return getCompatDrawable(getResourceIdFromAttributes(
        attributes = intArrayOf(R.attr.selectableItemBackgroundBorderless),
        index = 0
    ))
}


fun Context.getStateListDrawable(
    @DrawableRes pressedStateDrawableId: Int,
    @ColorInt pressedStateBackgroundColor: Int,
    @DrawableRes releasedStateDrawableId: Int,
    @ColorInt releasedStateBackgroundColor: Int
): StateListDrawable {
    val stateListDrawable = StateListDrawable()

    // Pressed state
    val pressedStateDrawable = (getCompatDrawable(pressedStateDrawableId) as GradientDrawable)
    pressedStateDrawable.setColor(pressedStateBackgroundColor)

    // Released state
    val releasedStateDrawable = (getCompatDrawable(releasedStateDrawableId) as GradientDrawable)
    releasedStateDrawable.setColor(releasedStateBackgroundColor)

    // Adding the actual states
    stateListDrawable.addState(intArrayOf(android.R.attr.state_pressed), pressedStateDrawable)
    stateListDrawable.addState(intArrayOf(android.R.attr.state_selected), pressedStateDrawable)
    stateListDrawable.addState(intArrayOf(), releasedStateDrawable)

    return stateListDrawable
}


fun Context.getLayerDrawable(
    @DrawableRes drawableId: Int,
    @ColorInt backgroundColor: Int,
    @ColorInt foregroundColor: Int
): Drawable {
    val layerDrawable = (getCompatDrawable(drawableId) as LayerDrawable)

    // Background
    val backgroundDrawable = (layerDrawable.findDrawableByLayerId(R.id.background) as GradientDrawable)
    backgroundDrawable.setColor(backgroundColor)

    // Foreground
    val foregroundDrawable = (layerDrawable.findDrawableByLayerId(R.id.foreground) as GradientDrawable)
    foregroundDrawable.setColor(foregroundColor)

    return layerDrawable
}


fun Context.getPrimaryButtonBackground(
    @ColorInt pressedStateBackgroundColor: Int,
    @ColorInt releasedStateBackgroundColor: Int
) : Drawable? {
    return getStateListDrawable(
        R.drawable.button_bg_state_pressed,
        pressedStateBackgroundColor,
        R.drawable.button_bg_state_released,
        releasedStateBackgroundColor
    )
}


fun Context.getNewPrimaryButtonBackground(
    @ColorInt backgroundColor: Int
) : Drawable? {
    val cornerRadius = getDimension(R.dimen.button_corner_radius)

    return getRoundedButtonBackgroundDrawable(
        backgroundColor = backgroundColor,
        cornerRadius = cornerRadius
    )
}


fun Context.getSecondaryButtonBackground(
    @ColorInt backgroundColor: Int,
    @ColorInt foregroundColor: Int
): Drawable? {
    return getLayerDrawable(
        R.drawable.secondary_button_background,
        backgroundColor,
        foregroundColor
    )
}


fun Context.getPortraitParticlesDrawable(
    @ColorInt gradientStartColor: Int,
    @ColorInt gradientEndColor: Int
): Drawable {
    val layerDrawable = (getCompatDrawable(R.drawable.particles_gradient_port_bg) as LayerDrawable)

    // Background
    val backgroundDrawable = (layerDrawable.findDrawableByLayerId(R.id.background) as GradientDrawable)
    backgroundDrawable.setGradientColors(gradientStartColor, gradientEndColor)

    return layerDrawable
}


fun Context.getCursorDrawable(@ColorInt color: Int): Drawable? {
    return getColoredCompatDrawable(R.drawable.edit_text_cursor_drawable, color)
}


fun Context.getDottedLineDrawable(@ColorInt color: Int): Drawable {
    val drawable = (getCompatDrawable(R.drawable.dotted_line_drawable) as GradientDrawable)
    drawable.setStroke(
        dimenInPx(R.dimen.dotted_line_separator_stroke_width),
        color,
        getDimension(R.dimen.dotted_line_separator_dash_width),
        getDimension(R.dimen.dotted_line_separator_dash_gap)
    )

    return drawable
}


fun Context.getPinBoxBorderDrawable(@ColorInt borderColor: Int): Drawable? {
    val drawable = (getCompatDrawable(R.drawable.pin_box_border_drawable) as GradientDrawable)
    drawable.setStroke(dimenInPx(R.dimen.pin_entry_box_border_width), borderColor)

    return drawable
}


fun Context.getPinBoxSolidDrawable(@ColorInt color: Int): Drawable? {
    return getColoredCompatDrawable(R.drawable.pin_box_solid_drawable, color)
}


fun Context.getPinEntryDigitButtonDrawable(@ColorInt color: Int): Drawable {
    val layerDrawable = (getCompatDrawable(R.drawable.pin_entry_digit_button_drawable) as LayerDrawable)

    // Background
    val backgroundDrawable = (layerDrawable.findDrawableByLayerId(R.id.background) as GradientDrawable)
    backgroundDrawable.setColor(color)

    return layerDrawable
}


fun Context.getPinEntryActionButtonDrawable(
    @DrawableRes drawableId: Int,
    @ColorInt backgroundColor: Int,
    @ColorInt foregroundColor: Int
): Drawable {
    val layerDrawable = (getCompatDrawable(drawableId) as LayerDrawable)

    // Background
    val backgroundDrawable = (layerDrawable.findDrawableByLayerId(R.id.background) as GradientDrawable)
    backgroundDrawable.setColor(backgroundColor)

    // Foreground
    val foregroundDrawable = (layerDrawable.findDrawableByLayerId(R.id.foreground) as VectorDrawable)
    foregroundDrawable.setColor(foregroundColor)

    return layerDrawable
}


fun Context.getPinEntryFingerprintButtonDrawable(@ColorInt backgroundColor: Int,
                                                 @ColorInt foregroundColor: Int): Drawable {
    return getPinEntryActionButtonDrawable(
        R.drawable.pin_entry_fingerprint_button_drawable,
        backgroundColor,
        foregroundColor
    )
}


fun Context.getPinEntryDeleteButtonDrawable(@ColorInt backgroundColor: Int,
                                            @ColorInt foregroundColor: Int): Drawable {
    return getPinEntryActionButtonDrawable(
        R.drawable.pin_entry_delete_button_drawable,
        backgroundColor,
        foregroundColor
    )
}


fun Context.getUserAdmissionButtonDrawable(@ColorInt color: Int): Drawable {
    val layerDrawable = (getCompatDrawable(R.drawable.user_admission_button_background_drawable) as LayerDrawable)

    // Main
    val mainDrawable = (layerDrawable.findDrawableByLayerId(R.id.main) as GradientDrawable)
    mainDrawable.setColor(color)

    return layerDrawable
}


fun Context.getOrderbookOrderBackgroundDrawable(
    @ColorInt color: Int,
    gravity: Int,
    @IntRange(from = 0L, to = 10000L) level: Int
) : Drawable {
    val clipDrawable = ClipDrawable(color.toDrawable(), gravity, ClipDrawable.HORIZONTAL).apply {
        setLevel(level)
    }
    val selectableItemBackgroundDrawable = getSelectableItemBackgroundDrawable()

    return LayerDrawable(arrayOf(
        clipDrawable,
        selectableItemBackgroundDrawable
    ))
}


private fun Context.getRoundedButtonBackgroundDrawable(
    @ColorInt backgroundColor: Int,
    cornerRadiiArray: FloatArray
) : LayerDrawable {
    val layerDrawable = (getCompatDrawable(R.drawable.rounded_button_background) as LayerDrawable)

    // Background
    (layerDrawable.findDrawableByLayerId(R.id.background) as GradientDrawable).apply {
        mutate()

        setColor(backgroundColor)
        cornerRadii = cornerRadiiArray
    }

    // Foreground
    (layerDrawable.findDrawableByLayerId(R.id.foreground) as RippleDrawable).apply {
        (findDrawableByLayerId(android.R.id.mask) as GradientDrawable).apply {
            mutate()

            cornerRadii = cornerRadiiArray
        }
    }

    return layerDrawable
}


fun Context.getRoundedButtonBackgroundDrawable(
    @ColorInt backgroundColor: Int,
    cornerRadius: Float
) : LayerDrawable {
    return getRoundedButtonBackgroundDrawable(
        backgroundColor = backgroundColor,
        cornerRadiiArray = floatArrayOf(
            cornerRadius, cornerRadius,
            cornerRadius, cornerRadius,
            cornerRadius, cornerRadius,
            cornerRadius, cornerRadius
        )
    )
}


fun Context.getRoundedButtonBackgroundDrawable(
    @ColorInt disabledStateBackgroundColor: Int,
    @ColorInt enabledStateBackgroundColor: Int,
    cornerRadiiArray: FloatArray
) : StateListDrawable {
    val stateListDrawable = StateListDrawable()

    // Disabled state
    val disabledStateDrawable = getRoundedButtonBackgroundDrawable(
        backgroundColor = disabledStateBackgroundColor,
        cornerRadiiArray = cornerRadiiArray
    )

    // Enabled state
    val enabledStateDrawable = getRoundedButtonBackgroundDrawable(
        backgroundColor = enabledStateBackgroundColor,
        cornerRadiiArray = cornerRadiiArray
    )

    // Adding actual states
    stateListDrawable.addState(intArrayOf(-android.R.attr.state_enabled), disabledStateDrawable)
    stateListDrawable.addState(intArrayOf(), enabledStateDrawable)

    return stateListDrawable
}


/**
 * Retrieves a default rounded button background drawable that has all four
 * corners rounded.
 *
 * @param disabledStateBackgroundColor The background color of the disabled state
 * @param enabledStateBackgroundColor The background color of the enabled state
 *
 * @return The state list drawable
 */
fun Context.getRoundedButtonBackgroundDrawable(
    @ColorInt disabledStateBackgroundColor: Int,
    @ColorInt enabledStateBackgroundColor: Int
) : StateListDrawable {
    val cornerRadius = getDimension(R.dimen.rounded_button_corner_radius)

    return getRoundedButtonBackgroundDrawable(
        disabledStateBackgroundColor = disabledStateBackgroundColor,
        enabledStateBackgroundColor = enabledStateBackgroundColor,
        cornerRadiiArray = floatArrayOf(
            cornerRadius, cornerRadius,
            cornerRadius, cornerRadius,
            cornerRadius, cornerRadius,
            cornerRadius, cornerRadius
        )
    )
}


/**
 * Retrieves a button background drawable that has the left side rounded.
 *
 * @param disabledStateBackgroundColor The background color of the disabled state
 * @param enabledStateBackgroundColor The background color of the enabled state
 *
 * @return The state list drawable
 */
fun Context.getLeftRoundedButtonBackgroundDrawable(
    @ColorInt disabledStateBackgroundColor: Int,
    @ColorInt enabledStateBackgroundColor: Int
) : StateListDrawable {
    val cornerRadius = getDimension(R.dimen.rounded_button_corner_radius)

    return getRoundedButtonBackgroundDrawable(
        disabledStateBackgroundColor,
        enabledStateBackgroundColor,
        floatArrayOf(
            cornerRadius,
            cornerRadius,
            0f, 0f, 0f, 0f,
            cornerRadius,
            cornerRadius
        )
    )
}


/**
 * Retrieves a button background drawable that has the right side rounded.
 *
 * @param disabledStateBackgroundColor The background color of the disabled state
 * @param enabledStateBackgroundColor The background color of the enabled state
 *
 * @return The state list drawable
 */
fun Context.getRightRoundedButtonBackgroundDrawable(
    @ColorInt disabledStateBackgroundColor: Int,
    @ColorInt enabledStateBackgroundColor: Int
) : StateListDrawable {
    val cornerRadius = getDimension(R.dimen.rounded_button_corner_radius)

    return getRoundedButtonBackgroundDrawable(
        disabledStateBackgroundColor,
        enabledStateBackgroundColor,
        floatArrayOf(
            0f, 0f,
            cornerRadius,
            cornerRadius,
            cornerRadius,
            cornerRadius,
            0f, 0f
        )
    )
}


fun Context.getBorderedButtonBackground(
    @ColorInt color: Int,
    cornerRadiiArray: FloatArray
) : LayerDrawable {
    val layerDrawable = (getCompatDrawable(R.drawable.bordered_button_background) as LayerDrawable)

    // Background
    (layerDrawable.findDrawableByLayerId(R.id.background) as GradientDrawable).apply {
        mutate()

        setStroke(
            dimenInPx(R.dimen.bordered_button_background_stroke_width),
            color,
            0f,
            0f
        )
        cornerRadii = cornerRadiiArray
    }

    // Foreground
    (layerDrawable.findDrawableByLayerId(R.id.foreground) as RippleDrawable).apply {
        (findDrawableByLayerId(android.R.id.mask) as GradientDrawable).apply {
            mutate()

            cornerRadii = cornerRadiiArray
        }
    }

    return layerDrawable
}


fun Context.getBorderedButtonBackground(
    @ColorInt color: Int
) : LayerDrawable {
    val cornerRadius = getDimension(R.dimen.button_corner_radius)

    return getBorderedButtonBackground(
        color = color,
        cornerRadiiArray = floatArrayOf(
            cornerRadius, cornerRadius,
            cornerRadius, cornerRadius,
            cornerRadius, cornerRadius,
            cornerRadius, cornerRadius
        )
    )
}


fun Context.getSelectableButtonBackground(@ColorInt color: Int): Drawable {
    val layerDrawable = (getCompatDrawable(R.drawable.selectable_button_background_drawable) as LayerDrawable)

    // Main
    val mainDrawable = (layerDrawable.findDrawableByLayerId(R.id.main) as GradientDrawable)
    mainDrawable.setColor(color)

    return layerDrawable
}


fun Context.getCardViewButtonTextSelector(
    @ColorInt disabledStateTextColor: Int,
    @ColorInt enabledStateTextColor: Int
) : ColorStateList {
    val states = arrayOf(
        intArrayOf(-android.R.attr.state_enabled),
        intArrayOf()
    )

    val colors = intArrayOf(
        disabledStateTextColor,
        enabledStateTextColor
    )

    return ColorStateList(states, colors)
}


fun Context.getButtonTextSelector(
    @ColorInt pressedStateTextColor: Int,
    @ColorInt releasedStateTextColor: Int
) : ColorStateList {
    val states = arrayOf(
        intArrayOf(android.R.attr.state_pressed),
        intArrayOf()
    )

    val colors = intArrayOf(
        pressedStateTextColor,
        releasedStateTextColor
    )

    return ColorStateList(states, colors)
}


fun Context.getFloatingButtonBackground(
    @ColorInt color: Int
) : Drawable {
    return getSelectableButtonBackground(color)
}


fun Context.getReferenceButtonBackground(
    @ColorInt color: Int
) : Drawable {
    val layerDrawable = (getCompatDrawable(R.drawable.reference_button_view_background) as LayerDrawable)

    // Background
    (layerDrawable.findDrawableByLayerId(R.id.background) as GradientDrawable).apply {
        mutate()

        setColor(color)
    }

    return layerDrawable
}


fun Context.getInputViewExtraViewBackground(
    @ColorInt color: Int
) : Drawable {
    val layerDrawable = (getCompatDrawable(R.drawable.input_view_extra_view_background_drawable) as LayerDrawable)

    // Background
    (layerDrawable.findDrawableByLayerId(R.id.background) as GradientDrawable).apply {
        mutate()

        setColor(color)
    }

    return layerDrawable
}


fun Context.getColoredSelectableItemBackgroundDrawable(
    @ColorInt color: Int
) : Drawable {
    val backgroundDrawable = ColorDrawable(color)
    val foregroundDrawable = getSelectableItemBackgroundDrawable()

    return LayerDrawable(arrayOf(
        backgroundDrawable,
        foregroundDrawable
    ))
}


fun Context.getKeyboardNumericKeyListener(): KeyListener? {
    return if(isGoogleKeyboard()) {
        val keys = StringBuilder("1234567890").apply {
            append(NumberFormatter.FORMATTER_DECIMAL_SYMBOL)
            append(NumberFormatter.FORMATTER_GROUPING_SYMBOL)
        }.toString()

        DigitsKeyListener.getInstance(keys)
    } else {
        null
    }
}


/**
 * Builds an instance of the [MaterialDialog] class.
 *
 * @param builder The builder model class holding all
 * the properties and values of the dialog
 * @param theme The application's theme to color the dialog
 *
 * @return The instance of the MaterialDialog class
 */
fun Context.buildMaterialDialog(
    builder: MaterialDialogBuilder,
    theme: Theme
): MaterialDialog {
    return MaterialDialog.Builder(this).run {
        if(builder.hasTitle) {
            title(builder.title)
        }

        if(builder.hasContent) {
            content(builder.content)
        }

        if(builder.hasItems) {
            items(*builder.items)

            if(builder.hasItemsCallback) {
                itemsCallback { _, _, _, text ->
                    builder.itemsCallback?.invoke(text.toString())
                }
            }

            if(builder.hasItemsCallbackSingleChoice && builder.hasSelectedItemIndex) {
                itemsCallbackSingleChoice(builder.selectedItemIndex) { _, _, index, _ ->
                    builder.itemsCallbackSingleChoice?.invoke(index)
                    true
                }
            }
        }

        if(builder.hasNegativeBtnText) {
            negativeText(builder.negativeBtnText)
        }

        if(builder.hasPositiveBtnText) {
            positiveText(builder.positiveBtnText)
        }

        cancelable(builder.isCancelable)

        onNegative { _, _ -> builder.negativeBtnClick?.invoke() }
        onPositive { _, _ -> builder.positiveBtnClick?.invoke() }

        ThemingUtil.Dialogs.dialogBuilder(this, theme)

        build()
    }
}