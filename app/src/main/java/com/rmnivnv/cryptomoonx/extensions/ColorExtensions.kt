package com.rmnivnv.cryptomoonx.extensions

import android.graphics.Color

fun Int.addAlpha(percent: Double): Int {
    var alpha = Color.alpha(this)
    val red = Color.red(this)
    val green = Color.green(this)
    val blue = Color.blue(this)

    alpha = (alpha * percent).toInt()

    return Color.argb(alpha, red, green, blue)
}