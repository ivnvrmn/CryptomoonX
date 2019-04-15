package com.rmnivnv.cryptomoonx.views

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.rmnivnv.cryptomoonx.R
import com.rmnivnv.cryptomoonx.extensions.dpToPx
import com.rmnivnv.cryptomoonx.extensions.spToPx

class CoinListItemView
@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply { textAlign = Paint.Align.LEFT }

    private val colorText = ContextCompat.getColor(context, R.color.text_color)
    private val colorPrimary = ContextCompat.getColor(context, R.color.color_primary)
    private val colorRed = ContextCompat.getColor(context, R.color.red)
    private val colorGreen = ContextCompat.getColor(context, R.color.green)
    private val textSizeTitle = TEXT_SIZE_TITLE_SP.spToPx(resources)
    private val textSizePrice = TEXT_SIZE_PRICE_SP.spToPx(resources)

    private var logoBitmap: Bitmap? = null
    private var title: String? = null
    private var price: String? = null
    private var percent: String? = null
    private var isPositivePercent = false

    private val marginInPx = MARGIN_DP.dpToPx(resources)
    private val marginTextInPx = MARGIN_TEXT_DP.dpToPx(resources)
    private val logoSizePx = LOGO_SIZE_DP.dpToPx(resources)
    private val titleTextXPosition = (marginInPx + logoSizePx + marginInPx).toFloat()
    private val separatorLineWidth = SEPARATOR_LINE_WIDTH_DP.dpToPx(resources).toFloat()

    fun setData(
        title: String,
        price: String,
        percent: String,
        isPositive: Boolean
    ) {
        this.title = title
        this.price = price
        this.percent = percent
        this.isPositivePercent = isPositive

        invalidate()
    }

    fun setLogo(logo: Bitmap) {
        logoBitmap = logo

        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = VIEW_HEIGHT_DP.dpToPx(resources)
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        drawLogo(canvas)
        drawSeparatorLine(canvas)
        drawTitle(canvas)
        drawPrice(canvas)
        drawPercent(canvas)
    }

    private fun drawSeparatorLine(canvas: Canvas) {
        setPaintToDrawSeparatorLine()
        canvas.drawLine(titleTextXPosition, height.toFloat(), (width - marginInPx).toFloat(), height.toFloat(), paint)
    }

    private fun setPaintToDrawSeparatorLine() = with(paint) {
        style = Paint.Style.STROKE
        strokeWidth = separatorLineWidth
        color = colorPrimary
    }

    private fun drawTitle(canvas: Canvas) {
        title?.also {
            setTextPaintToDrawTitle()
            val y = height / 2 - marginTextInPx
            canvas.drawText(it, titleTextXPosition, y.toFloat(), textPaint)
        }
    }

    private fun setTextPaintToDrawTitle() = with(textPaint) {
        color = colorText
        textSize = textSizeTitle
    }

    private fun drawPrice(canvas: Canvas) {
        price?.also {
            setTextPaintToDrawPrice()
            val y = height / 2 + textSizeTitle
            canvas.drawText(it, titleTextXPosition, y, textPaint)
        }
    }

    private fun setTextPaintToDrawPrice() = with(textPaint) {
        color = colorText
        textSize = textSizePrice
    }

    private fun drawPercent(canvas: Canvas) {
        percent?.also {
            drawPercentText(canvas, it)
        }
    }

    private fun getPercentColor(): Int = if (isPositivePercent) colorGreen else colorRed

    private fun drawPercentText(canvas: Canvas, text: String) {
        setTextPaintToDrawPercent()
        val percentTextWidth = textPaint.measureText(text)
        val x = width - marginInPx - percentTextWidth
        val y = height / 2 + textSizeTitle
        canvas.drawText(text, x, y, textPaint)
    }

    private fun setTextPaintToDrawPercent() = with(textPaint) {
        color = getPercentColor()
        textSize = textSizeTitle
    }

    private fun drawLogo(canvas: Canvas) {
        logoBitmap?.also {
            canvas.drawBitmap(it, marginInPx.toFloat(), marginInPx.toFloat(), null)
        }
    }

    companion object {
        const val LOGO_SIZE_DP = 48f

        private const val VIEW_HEIGHT_DP = 80f
        private const val MARGIN_DP = 16f
        private const val MARGIN_TEXT_DP = 8f
        private const val TEXT_SIZE_TITLE_SP = 18f
        private const val TEXT_SIZE_PRICE_SP = 24f
        private const val SEPARATOR_LINE_WIDTH_DP = 1f
    }
}