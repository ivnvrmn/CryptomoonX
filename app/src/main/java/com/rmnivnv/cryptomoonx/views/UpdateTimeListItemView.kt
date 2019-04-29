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

private const val TEXT_SIZE_SP = 14f
private const val VIEW_HEIGHT_DP = 24f
private const val DATE_FULL_PATTERN = "dd MMM HH:mm:ss"
private const val DATE_DAY_OF_MONTH_PATTERN = "dd"

class UpdateTimeListItemView
@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.LEFT
        color = ContextCompat.getColor(context, R.color.text_color)
        textSize = TEXT_SIZE_SP.spToPx(resources)
    }
    private val title = context.getString(R.string.update_time_title)
    private val calendar = Calendar.getInstance()
    private val dateFullFormat = SimpleDateFormat(DATE_FULL_PATTERN, Locale.getDefault())
    private val dateDayOfMonthFormat = SimpleDateFormat(DATE_DAY_OF_MONTH_PATTERN, Locale.getDefault())
    private var totalTitle = ""
    private var dayOfMonth = "0"
    private var dayOfMonthUpdated = false

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = VIEW_HEIGHT_DP.dpToPx(resources)
        setMeasuredDimension(width, height)
    }

    fun setUpdatedTime(time: Long) {
        calendar.timeInMillis = time
        totalTitle = "$title ${dateFullFormat.format(calendar.time)}"

        calendar.get(Calendar.DAY_OF_MONTH).also {
            dayOfMonthUpdated = it != dayOfMonth.toInt()
            dayOfMonth = dateDayOfMonthFormat.format(calendar.time)
        }

        calendar.get(Calendar.MONTH).also {

        }
    }

    override fun onDraw(canvas: Canvas) {

    }

    private fun drawTitle(canvas: Canvas) {

    }

    private fun drawMonthDays(canvas: Canvas) {

    }

    private fun drawMonth(canvas: Canvas) {

    }

    private fun drawHours(canvas: Canvas) {

    }

    private fun drawMinutes(canvas: Canvas) {

    }

    private fun drawSeconds(canvas: Canvas) {

    }
}