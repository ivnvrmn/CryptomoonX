package com.rmnivnv.cryptomoonx.extensions

import android.content.res.Resources

fun Float.dpToPx(resources: Resources): Int {
    return (this * resources.displayMetrics.density).toInt()
}

fun Float.spToPx(resources: Resources): Float {
    return this * resources.displayMetrics.scaledDensity
}

fun Float.pxToDp(resources: Resources): Int {
    return (this / resources.displayMetrics.density).toInt()
}