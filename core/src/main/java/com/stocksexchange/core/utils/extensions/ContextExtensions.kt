@file:Suppress("NOTHING_TO_INLINE")

package com.stocksexchange.core.utils.extensions

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.AlarmManager
import android.app.NotificationManager
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.ShortcutManager
import android.content.res.Configuration
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.PowerManager
import android.provider.Settings
import android.telephony.TelephonyManager
import android.text.InputType
import android.view.LayoutInflater
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.hardware.fingerprint.FingerprintManagerCompat
import com.stocksexchange.core.Constants
import com.stocksexchange.core.model.NetworkGeneration
import com.stocksexchange.core.model.NetworkInfo
import com.stocksexchange.core.model.NetworkType
import com.stocksexchange.core.model.ScreenSize
import java.util.*
import kotlin.math.roundToInt


/**
 * Property to access the context itself.
 */
inline val Context.ctx: Context
    get() = this


/**
 * Converts a DP dimension to pixels.
 *
 * @param value The value to convert to pixels
 *
 * @return The converted dimension in pixels
 */
fun Context.dpToPx(value: Int): Int = (value * resources.displayMetrics.density).toInt()


/**
 * Converts a DP dimension to pixels.
 *
 * @param value The value to convert to pixels
 *
 * @return The converted dimension in pixels
 */
fun Context.dpToPx(value: Float): Float = (value * resources.displayMetrics.density)


/**
 * Converts an SP dimension to pixels.
 *
 * @param value The value to convert to pixels
 *
 * @return The converted dimension in pixels
 */
fun Context.spToPx(value: Int): Int = (value * resources.displayMetrics.scaledDensity).toInt()


/**
 * Converts an SP dimension to pixels.
 *
 * @param value The value to convert to pixels
 *
 * @return The converted dimension in pixels
 */
fun Context.spToPx(value: Float): Float = (value * resources.displayMetrics.scaledDensity)


/**
 * Retrieves a dimension resource in integer pixels.
 *
 * @param resourceId The resource ID to fetch
 *
 * @return The fetched resource in integer pixels
 */
fun Context.dimenInPx(@DimenRes resourceId: Int): Int = resources.getDimensionPixelSize(resourceId)


/**
 * Retrieves a resource ID from the specific attributes.
 *
 * @param attributes The attributes to fetch a resource ID from
 * @param index The index of the resource ID
 *
 * @return The resource ID
 */
fun Context.getResourceIdFromAttributes(attributes: IntArray, index: Int): Int {
    val typedArray = obtainStyledAttributes(attributes)
    val resourceId = typedArray.getResourceId(index, 0)

    typedArray.recycle()

    return resourceId
}


fun Context.getLayoutInflater(): LayoutInflater {
    return LayoutInflater.from(this)
}


/**
 * Retrieves a drawable in a backwards-compatible way.
 *
 * @param id The id of the drawable
 *
 * @return The drawable
 */
fun Context.getCompatDrawable(@DrawableRes id: Int): Drawable? {
    return AppCompatResources.getDrawable(this, id)
}


/**
 * Retrieves a drawable and colors it in a backwards-compatible way.
 *
 * @param drawableId The ID of the drawable
 * @param color The color for the drawable
 *
 * @return The colored drawable
 */
fun Context.getColoredCompatDrawable(@DrawableRes drawableId: Int, @ColorInt color: Int): Drawable? {
    return getCompatDrawable(drawableId)?.apply {
        setColor(color)
    }
}


/**
 * Retrieves a color in a backwards-compatible way.
 *
 * @param id The ID of the color
 *
 * @return The color
 */
fun Context.getCompatColor(@ColorRes id: Int): Int {
    return ContextCompat.getColor(this, id)
}


fun Context.getDimension(@DimenRes id: Int): Float {
    return resources.getDimension(id)
}


fun Context.getStatusBarHeight(): Int {
    with(resources) {
        return getDimensionPixelSize(getIdentifier("status_bar_height", "dimen", "android"))
    }
}


@SuppressWarnings("NewApi")
fun Context.getLocale(): Locale {
    return if(Constants.AT_LEAST_NOUGAT_V1) {
        resources.configuration.locales[0]
    } else {
        resources.configuration.locale
    }
}


@SuppressWarnings("NewApi")
fun Context.getLocales(): List<Locale> {
    return if(Constants.AT_LEAST_NOUGAT_V1) {
        val locales = resources.configuration.locales
        val localeList = mutableListOf<Locale>()

        for(i in 0 until locales.size()) {
            localeList.add(locales.get(i))
        }

        localeList
    } else {
        listOf(resources.configuration.locale)
    }
}


/**
 * Returns a context with a specified locale.
 *
 * @param locale The locale of the context
 *
 * @return The context with a specified locale
 */
fun Context.createContextWithLocale(locale: Locale): Context {
    val configuration = Configuration(resources.configuration).apply {
        setLocale(locale)
    }

    return createConfigurationContext(configuration)
}


/**
 * Returns a context with a specified smallest screen width in DP in
 * a specified orientation.
 *
 * @param smallestScreenWidthDp The smallest screen width in DP
 * @param orientation The orientation. One of Configuration.ORIENTATION_*
 * constants.
 *
 * @return The context with the specified parameters
 */
fun Context.createContextWithSsw(smallestScreenWidthDp: Int, orientation: Int): Context {
    val configuration = Configuration(resources.configuration).apply {
        this.smallestScreenWidthDp = smallestScreenWidthDp
        this.orientation = orientation
    }

    return createConfigurationContext(configuration)
}


fun Context.isNetworkAvailable(): Boolean {
    val connectivityManager = (getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)
    return connectivityManager.activeNetworkInfo?.isAvailable ?: false
}


/**
 * Checks whether the given intent can be handled by some activity or not.
 *
 * @param intent The intent to check
 *
 * @return true if can be handled; false otherwise
 */
fun Context.canIntentBeHandled(intent: Intent): Boolean {
    return packageManager.queryIntentActivities(intent, 0).isNotEmpty()
}


fun Context.getSmallestScreenWidthInDp(): Int {
    return (resources.displayMetrics.widthPixels / resources.displayMetrics.density).roundToInt()
}


fun Context.getNotificationManager(): NotificationManager {
    return (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
}


fun Context.getAlarmManager(): AlarmManager {
    return (getSystemService(Context.ALARM_SERVICE) as AlarmManager)
}


fun Context.getPowerManager(): PowerManager {
    return (getSystemService(Context.POWER_SERVICE) as PowerManager)
}


@SuppressWarnings("NewApi")
fun Context.getShortcutManager(): ShortcutManager {
    return (getSystemService(Context.SHORTCUT_SERVICE) as ShortcutManager)
}


fun Context.getActivityManager(): ActivityManager {
    return (getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager)
}


fun Context.getInputMethodManager(): InputMethodManager {
    return (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
}


fun Context.getCompatNotificationManager(): NotificationManagerCompat {
    return NotificationManagerCompat.from(this)
}


fun Context.getFingerprintManager(): FingerprintManagerCompat {
    return FingerprintManagerCompat.from(this)
}


fun Context.getClipboardManager(): ClipboardManager {
    return (getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager)
}


fun Context.getKeyboardNumericInputType(): Int {
    return if(isGoogleKeyboard()) {
        (InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)
    } else {
        (InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS)
    }
}


fun Context.isGoogleKeyboard(): Boolean {
    return Settings.Secure.getString(contentResolver, Settings.Secure.DEFAULT_INPUT_METHOD)
        .toLowerCase()
        .contains("google")
}


/**
 * Returns the size of the screen.
 *
 * @return One of [ScreenSize] constants
 */
fun Context.getScreenSize(): ScreenSize {
    val screenLayout = (resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK)

    return when(screenLayout) {
        Configuration.SCREENLAYOUT_SIZE_SMALL -> ScreenSize.SMALL
        Configuration.SCREENLAYOUT_SIZE_NORMAL -> ScreenSize.NORMAL
        Configuration.SCREENLAYOUT_SIZE_LARGE -> ScreenSize.LARGE
        Configuration.SCREENLAYOUT_SIZE_XLARGE -> ScreenSize.XLARGE

        else -> ScreenSize.UNDEFINED
    }
}


@SuppressLint("InlinedApi")
fun Context.getNetworkGeneration(): NetworkGeneration {
    val telephonyManager = (getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager)
    val secondGenNetworks = mutableListOf<Int>().apply {
        add(TelephonyManager.NETWORK_TYPE_GPRS)
        add(TelephonyManager.NETWORK_TYPE_EDGE)
        add(TelephonyManager.NETWORK_TYPE_CDMA)
        add(TelephonyManager.NETWORK_TYPE_1xRTT)
        add(TelephonyManager.NETWORK_TYPE_IDEN)

        if(Constants.AT_LEAST_NOUGAT_V2) {
            add(TelephonyManager.NETWORK_TYPE_GSM)
        }
    }
    val thirdGenNetworks = mutableListOf<Int>().apply {
        add(TelephonyManager.NETWORK_TYPE_UMTS)
        add(TelephonyManager.NETWORK_TYPE_EVDO_0)
        add(TelephonyManager.NETWORK_TYPE_EVDO_A)
        add(TelephonyManager.NETWORK_TYPE_HSDPA)
        add(TelephonyManager.NETWORK_TYPE_HSUPA)
        add(TelephonyManager.NETWORK_TYPE_HSPA)
        add(TelephonyManager.NETWORK_TYPE_EVDO_B)
        add(TelephonyManager.NETWORK_TYPE_EHRPD)
        add(TelephonyManager.NETWORK_TYPE_HSPAP)

        if(Constants.AT_LEAST_NOUGAT_V2) {
            add(TelephonyManager.NETWORK_TYPE_TD_SCDMA)
        }
    }
    val fourthGenNetworks = mutableListOf<Int>().apply {
        add(TelephonyManager.NETWORK_TYPE_LTE)

        if(Constants.AT_LEAST_NOUGAT_V2) {
            add(TelephonyManager.NETWORK_TYPE_IWLAN)
        }
    }
    val fifthGenNetworks = mutableListOf<Int>().apply {
        if(Constants.AT_LEAST_10) {
            add(TelephonyManager.NETWORK_TYPE_NR)
        }
    }

    return when(telephonyManager.networkType) {
        in secondGenNetworks -> NetworkGeneration.SECOND_GEN
        in thirdGenNetworks -> NetworkGeneration.THIRD_GEN
        in fourthGenNetworks -> NetworkGeneration.FOURTH_GEN
        in fifthGenNetworks -> NetworkGeneration.FIFTH_GEN

        else -> NetworkGeneration.UNKNOWN
    }
}


@SuppressLint("NewApi")
fun Context.getNetworkType(): NetworkType {
    if(!isNetworkAvailable()) {
        return NetworkType.NOT_AVAILABLE
    }

    val cm = (getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)

    if(Constants.AT_LEAST_MARSHMALLOW) {
        val networkCapabilities = (cm.getNetworkCapabilities(cm.activeNetwork) ?: return NetworkType.UNKNOWN)

        return when {
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> NetworkType.WIFI
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> NetworkType.CELLULAR

            else -> NetworkType.UNKNOWN
        }
    } else {
        val activeNetworkInfo = (cm.activeNetworkInfo ?: return NetworkType.UNKNOWN)

        return when(activeNetworkInfo.type) {
            ConnectivityManager.TYPE_WIFI -> NetworkType.WIFI
            ConnectivityManager.TYPE_MOBILE -> NetworkType.CELLULAR

            else -> NetworkType.UNKNOWN
        }
    }
}


fun Context.getNetworkInfo(): NetworkInfo {
    return NetworkInfo(
        networkType = getNetworkType(),
        networkGeneration = getNetworkGeneration()
    )
}


inline fun <reified T> Context.intentFor(): Intent = Intent(this, T::class.java)


inline fun Context.shortToast(message: CharSequence): Toast = Toast
    .makeText(this, message, Toast.LENGTH_SHORT).apply {
        show()
    }


inline fun Context.longToast(message: CharSequence): Toast = Toast
    .makeText(this, message, Toast.LENGTH_LONG).apply {
        show()
    }