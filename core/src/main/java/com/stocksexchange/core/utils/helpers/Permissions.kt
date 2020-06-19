package com.stocksexchange.core.utils.helpers

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.stocksexchange.core.Constants


/**
 * Checks whether all permission requests have been granted or not.
 *
 * @param grantResults The integer array of permission grant results
 *
 * @return true if all permissions have been granted; false otherwise
 */
fun isPermissionSetGranted(grantResults: IntArray): Boolean {
    return grantResults.all { it == PackageManager.PERMISSION_GRANTED }
}


/**
 * Checks whether the specified permission has been granted.
 *
 * @param permission The permission to check
 *
 * @return true if the permission has been granted; false otherwise
 */
fun Context.checkPermission(permission: String): Boolean {
    if(!Constants.AT_LEAST_MARSHMALLOW) {
        return true
    }

    return (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED)
}


/**
 * Checks whether the specified permissions have been granted.
 *
 * @param permissions The permissions to check
 *
 * @return true if all permissions have been granted; false otherwise
 */
fun Context.checkPermissions(permissions: Array<out String>): Boolean {
    return permissions.all { checkPermission(it) }
}


/**
 * Works similarly to [Activity.checkPermissions], but for fragments.
 *
 * @see Activity.checkPermissions
 */
fun Fragment.checkPermissions(requestCode: Int, permissions: Array<out String>): Boolean {
    return context
        ?.takeIf { !it.checkPermissions(permissions) }
        ?.apply { requestPermissions(permissions, requestCode) }
        ?.let { false } ?: true
}


/**
 * Checks whether the specified permissions have been granted. If granted,
 * returns true. If not, requests them and returns false.
 *
 * @param requestCode The request code for the permissions in case they are not granted
 * @param permissions The permissions to check and, if not granted, to request
 *
 * @return true if all permissions have been granted; false otherwise
 */
fun Activity.checkPermissions(requestCode: Int, permissions: Array<out String>): Boolean {
    return this
        .takeIf { !checkPermissions(permissions) }
        ?.apply { ActivityCompat.requestPermissions(this, permissions, requestCode) }
        ?.let { false } ?: true
}