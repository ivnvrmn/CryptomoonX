package com.rmnivnv.cryptomoonx.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.rmnivnv.cryptomoonx.R
import com.rmnivnv.cryptomoonx.extensions.dpToPx
import com.rmnivnv.cryptomoonx.extensions.spToPx
import java.text.SimpleDateFormat
import java.util.*

private const val TEXT_SIZE_SP = 12f
private const val VIEW_HEIGHT_DP = 24f
private const val DATE_FULL_PATTERN = "dd MMM HH:mm:ss"

class UpdateTimeListItemView
@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.CENTER
        color = ContextCompat.getColor(context, R.color.text_color)
        textSize = TEXT_SIZE_SP.spToPx(resources)
    }
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = resources.getDimension(R.dimen.separator_line_width)
        color = ContextCompat.getColor(context, R.color.color_primary)
    }
    private val calendar = Calendar.getInstance()
    private val dateFullFormat = SimpleDateFormat(DATE_FULL_PATTERN, Locale.US)

    private val title = context.getString(R.string.update_time_title)
    private val margin = resources.getDimension(R.dimen.margin_default)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = VIEW_HEIGHT_DP.dpToPx(resources)
        setMeasuredDimension(width, height)
    }

    fun setUpdatedTime(time: Long) {
        calendar.timeInMillis = time
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        drawText(canvas)
        drawLine(canvas)
    }

    private fun drawText(canvas: Canvas) {
        val text = "$title ${dateFullFormat.format(calendar.time)}"
        val x = (width / 2).toFloat()
        val y = height - margin
        canvas.drawText(text, x, y, textPaint)
    }

    private fun drawLine(canvas: Canvas) {
        canvas.drawLine(0f, height.toFloat(), width.toFloat(), height.toFloat(), paint)
    }
}