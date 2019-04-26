package com.rmnivnv.cryptomoonx.views

import android.content.Context
import android.graphics.Paint
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.rmnivnv.cryptomoonx.R
import com.rmnivnv.cryptomoonx.extensions.spToPx

private const val TEXT_SIZE_SP = 14f

class UpdateTimeListItemView
@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply { textAlign = Paint.Align.LEFT }
    private val colorText = ContextCompat.getColor(context, R.color.text_color)
    private val textSize = TEXT_SIZE_SP.spToPx(resources)


}