package com.xavier.calculator.api

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.util.TypedValue
import android.widget.Toast
import androidx.annotation.StringRes
import com.xavier.calculator.MainApplication

///////////////////////////////////////////////////////////////////////////
// Screen
///////////////////////////////////////////////////////////////////////////

inline val screenWidth: Int get() = MainApplication.appContext.resources.displayMetrics.widthPixels

inline val isVerticalScreen: Boolean get() = MainApplication.appContext.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT

///////////////////////////////////////////////////////////////////////////
// Size
///////////////////////////////////////////////////////////////////////////

inline val Int.dpToPx: Float get() = toFloat().dpToPx

inline val Float.dpToPx: Float
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        Resources.getSystem().displayMetrics
    )

fun Int.pxToDp(): Int = toFloat().pxToDp()

fun Float.pxToDp(): Int =
    (this / Resources.getSystem().displayMetrics.density + 0.5f).toInt()

///////////////////////////////////////////////////////////////////////////
// Resource
///////////////////////////////////////////////////////////////////////////

fun @receiver:StringRes Int.string(context: Context = MainApplication.appContext): String =
    context.getString(this)

///////////////////////////////////////////////////////////////////////////
// Toast
///////////////////////////////////////////////////////////////////////////

fun @receiver:StringRes Int.toast(
    context: Context = MainApplication.appContext,
    duration: Int = LOGIC_INT_INIT_VALUE
) {
    string(context).toast(context, duration)
}

fun String?.toast(
    context: Context = MainApplication.appContext,
    duration: Int = LOGIC_INT_INIT_VALUE
) {
    if (isNullOrEmpty()) return
    val durationFinal = if (duration == Toast.LENGTH_SHORT || duration == Toast.LENGTH_LONG) {
        duration
    } else {
        if (length < 16) Toast.LENGTH_SHORT else Toast.LENGTH_LONG
    }
    Toast.makeText(context, this, durationFinal).show()
}