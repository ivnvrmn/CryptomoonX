package com.rmnivnv.cryptomoonx.views

import android.animation.AnimatorSet
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
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
    private val matrixCustom = Matrix()
    private val textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply { textAlign = Paint.Align.LEFT }

    private val colorText = ContextCompat.getColor(context, R.color.text_color)
    private val colorPrimary = ContextCompat.getColor(context, R.color.color_primary)
    private val colorRed = ContextCompat.getColor(context, R.color.red)
    private val colorGreen = ContextCompat.getColor(context, R.color.green)
    private val textSizeTitle = TEXT_SIZE_TITLE_SP.spToPx(resources)
    private val textSizeTitleClicked = TEXT_SIZE_TITLE_CLICKED_SP.spToPx(resources)
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
    private val separatorLineWidth = resources.getDimension(R.dimen.separator_line_width)

    private var percentPositionY = 0f
    private var titlePositionYClicked = 0f
    private var pricePositionYClicked = 0f

    private var isClicked = false
    private var onClickAnimatorSet = AnimatorSet()
    private var isSlided = false

    private lateinit var holderIconRotate: PropertyValuesHolder
    private var logoRotateValue = 0f
    private var logoAppearanceValue = -logoSizePx.toFloat()
    private val holderIconAppearance = PropertyValuesHolder.ofFloat(
        PROPERTY_ICON_APPEARANCE,
        -logoSizePx.toFloat(),
        marginInPx.toFloat()
    )
    private val logoAppearanceAnimator = ValueAnimator().apply {
        interpolator = AccelerateDecelerateInterpolator()
        addUpdateListener { animation ->
            logoAppearanceValue = animation.getAnimatedValue(PROPERTY_ICON_APPEARANCE) as Float
            logoRotateValue = animation.getAnimatedValue(PROPERTY_ICON_ROTATE) as Float

            invalidate()
        }
    }

    private lateinit var titleSizeHolder: PropertyValuesHolder
    private lateinit var titlePositionHolder: PropertyValuesHolder
    private var titleSizeValue = textSizeTitle
    private var titlePositionYValue = 0f
    private val titleAnimator = ValueAnimator().apply {
        interpolator = AccelerateDecelerateInterpolator()
        addUpdateListener { animation ->
            titleSizeValue = animation.getAnimatedValue(PROPERTY_TITLE_SIZE) as Float
            titlePositionYValue = animation.getAnimatedValue(PROPERTY_TITLE_POSITION) as Float
            logoRotateValue = animation.getAnimatedValue(PROPERTY_ICON_ROTATE) as Float

            invalidate()
        }
    }

    private lateinit var pricePositionHolder: PropertyValuesHolder
    private lateinit var percentPositionHolder: PropertyValuesHolder
    private lateinit var alphaTextHolder: PropertyValuesHolder
    private var pricePositionYValue = 0f
    private var percentPositionXValue = 0f
    private var alphaTextValue = ALPHA_VISIBLE
    private val hidePriceAnimator = ValueAnimator().apply {
        interpolator = AccelerateInterpolator()
        addUpdateListener { animation ->
            pricePositionYValue = animation.getAnimatedValue(PROPERTY_PRICE_POSITION) as Float
            percentPositionXValue = animation.getAnimatedValue(PROPERTY_PERCENT_POSITION) as Float
            alphaTextValue = animation.getAnimatedValue(PROPERTY_TEXT_ALPHA) as Int
        }
    }

    init {
        background = ContextCompat.getDrawable(context, R.drawable.ripple)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = VIEW_HEIGHT_DP.dpToPx(resources)

        titlePositionYValue = (height / 2 - marginTextInPx).toFloat()
        titlePositionYClicked = (height / 2 + marginTextInPx).toFloat()
        pricePositionYValue = height / 2 + textSizeTitle
        pricePositionYClicked = height + textSizePrice
        percentPositionY = height / 2 + textSizeTitle

        setMeasuredDimension(width, height)
    }

    fun setData(
        title: String,
        price: String,
        percent: String,
        isPositive: Boolean,
        isClicked: Boolean
    ) {
        this.title = title
        this.price = price
        this.percent = percent
        this.isPositivePercent = isPositive

        titlePositionYValue = if (isClicked) {
            (height / 2 + marginTextInPx).toFloat()
        } else {
            (height / 2 - marginTextInPx).toFloat()
        }
        titleSizeValue = if (isClicked) textSizeTitleClicked else textSizeTitle
        pricePositionYValue = if (isClicked) pricePositionYClicked else height / 2 + textSizeTitle
        percentPositionXValue = if (isClicked) PERCENT_POSITION_X_CLICKED else PERCENT_POSITION_X
        alphaTextValue = if (isClicked) ALPHA_INVISIBLE else ALPHA_VISIBLE

        invalidate()
    }

    fun setLogo(logo: Bitmap?) {
        logoBitmap = logo

        logo?.also {
            holderIconRotate = PropertyValuesHolder.ofFloat(PROPERTY_ICON_ROTATE, generateIconRotateAngle(), 0f)
            logoAppearanceAnimator.setValues(holderIconRotate, holderIconAppearance)
            logoAppearanceAnimator.start()
        } ?: invalidate()
    }

    fun onClicked() {
        if (onClickAnimatorSet.isRunning) return
        if (isSlided) {
            isSlided = false
            return
        }

        if (isClicked) {
            isClicked = false
            startOnClickAnimation()
        } else {
            isClicked = true
            startOnClickAnimation()
        }
    }

    fun onSlide(slideOffset: Float) {
        if (slideOffset.isNaN()) {
            titleSizeValue = textSizeTitleClicked
            titlePositionYValue = titlePositionYClicked
            pricePositionYValue = pricePositionYClicked
            percentPositionXValue = PERCENT_POSITION_X_CLICKED
            alphaTextValue = ALPHA_INVISIBLE
            logoRotateValue = ROTATE_ANGLE_CLICKED

            invalidate()
            return
        }

        titleSizeValue = textSizeTitleClicked + ((textSizeTitleClicked - textSizeTitle) * slideOffset)
        titlePositionYValue = titlePositionYClicked +
            ((titlePositionYClicked - (height / 2 - marginTextInPx)) * slideOffset)
        pricePositionYValue = pricePositionYClicked +
            ((pricePositionYClicked - (height / 2 + textSizeTitle)) * slideOffset)
        percentPositionXValue = PERCENT_POSITION_X_CLICKED +
            ((PERCENT_POSITION_X_CLICKED - PERCENT_POSITION_X) * slideOffset)
        alphaTextValue = 0 - (ALPHA_VISIBLE * slideOffset).toInt()
        logoRotateValue = ROTATE_ANGLE_CLICKED * slideOffset

        if (slideOffset == -1.0f) {
            isClicked = false
            isSlided = true
        }

        invalidate()
    }

    private fun startOnClickAnimation() {
        titleSizeHolder = PropertyValuesHolder.ofFloat(
            PROPERTY_TITLE_SIZE,
            if (isClicked) textSizeTitle else textSizeTitleClicked,
            if (isClicked) textSizeTitleClicked else textSizeTitle
        )
        titlePositionHolder = PropertyValuesHolder.ofFloat(
            PROPERTY_TITLE_POSITION,
            if (isClicked) titlePositionYValue else titlePositionYClicked,
            if (isClicked) titlePositionYClicked else (height / 2 - marginTextInPx).toFloat()
        )
        pricePositionHolder = PropertyValuesHolder.ofFloat(
            PROPERTY_PRICE_POSITION,
            if (isClicked) pricePositionYValue else pricePositionYClicked,
            if (isClicked) pricePositionYClicked else height / 2 + textSizeTitle
        )
        percentPositionHolder = PropertyValuesHolder.ofFloat(
            PROPERTY_PERCENT_POSITION,
            if (isClicked) PERCENT_POSITION_X else PERCENT_POSITION_X_CLICKED,
            if (isClicked) PERCENT_POSITION_X_CLICKED else PERCENT_POSITION_X
        )
        alphaTextHolder = PropertyValuesHolder.ofInt(
            PROPERTY_TEXT_ALPHA,
            if (isClicked) ALPHA_VISIBLE else ALPHA_INVISIBLE,
            if (isClicked) ALPHA_INVISIBLE else ALPHA_VISIBLE
        )
        holderIconRotate = PropertyValuesHolder.ofFloat(
            PROPERTY_ICON_ROTATE,
            if (isClicked) ROTATE_ANGLE else ROTATE_ANGLE_CLICKED,
            if (isClicked) ROTATE_ANGLE_CLICKED else ROTATE_ANGLE
        )

        titleAnimator.setValues(titleSizeHolder, titlePositionHolder, holderIconRotate)
        hidePriceAnimator.apply {
            interpolator = if (isClicked) AccelerateInterpolator() else DecelerateInterpolator()
            setValues(pricePositionHolder, percentPositionHolder, alphaTextHolder)
        }

        onClickAnimatorSet = AnimatorSet().apply {
            duration = DURATION_CLICK_ANIMATION
            playTogether(titleAnimator, hidePriceAnimator)
            start()
        }
    }

    override fun onDraw(canvas: Canvas) {
        drawLogo(canvas)
        drawSeparatorLine(canvas)
        drawTitle(canvas)
        drawPrice(canvas)
        drawPercent(canvas)
    }

    private fun drawLogo(canvas: Canvas) {
        logoBitmap?.also {
            matrixCustom.setTranslate(logoAppearanceValue, marginInPx.toFloat())
            matrixCustom.postRotate(
                logoRotateValue, logoAppearanceValue + (logoSizePx / 2), (marginInPx + (logoSizePx / 2)).toFloat()
            )
            canvas.drawBitmap(it, matrixCustom, null)
        }
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
            canvas.drawText(it, titleTextXPosition, titlePositionYValue, textPaint)
        }
    }

    private fun setTextPaintToDrawTitle() = with(textPaint) {
        color = colorText
        textSize = titleSizeValue
        alpha = ALPHA_VISIBLE
    }

    private fun drawPrice(canvas: Canvas) {
        price?.also {
            setTextPaintToDrawPrice()
            canvas.drawText(it, titleTextXPosition, pricePositionYValue, textPaint)
        }
    }

    private fun setTextPaintToDrawPrice() = with(textPaint) {
        color = colorText
        textSize = textSizePrice
        alpha = alphaTextValue
    }

    private fun drawPercent(canvas: Canvas) {
        percent?.also {
            setTextPaintToDrawPercent()
            val percentTextWidth = textPaint.measureText(it)
            val x = width - marginInPx - percentTextWidth + percentPositionXValue
            canvas.drawText(it, x, percentPositionY, textPaint)
        }
    }

    private fun getPercentColor(): Int = if (isPositivePercent) colorGreen else colorRed

    private fun setTextPaintToDrawPercent() = with(textPaint) {
        color = getPercentColor()
        textSize = textSizeTitle
        alpha = alphaTextValue
    }

    private fun generateIconRotateAngle() = (-180..180).random().toFloat()

    companion object {
        const val LOGO_SIZE_DP = 48f

        private const val VIEW_HEIGHT_DP = 80f
        private const val MARGIN_DP = 16f
        private const val MARGIN_TEXT_DP = 8f
        private const val TEXT_SIZE_TITLE_SP = 18f
        private const val TEXT_SIZE_TITLE_CLICKED_SP = 30f
        private const val TEXT_SIZE_PRICE_SP = 24f
        private const val ALPHA_VISIBLE = 255
        private const val ALPHA_INVISIBLE = 0
        private const val PERCENT_POSITION_X = 0f
        private const val PERCENT_POSITION_X_CLICKED = 300f
        private const val ROTATE_ANGLE = 0f
        private const val ROTATE_ANGLE_CLICKED = 360f

        private const val DURATION_CLICK_ANIMATION = 200L

        private const val PROPERTY_ICON_ROTATE = "propertyIconRotate"
        private const val PROPERTY_ICON_APPEARANCE = "propertyIconAppearance"
        private const val PROPERTY_TITLE_SIZE = "propertyTitleSize"
        private const val PROPERTY_TITLE_POSITION = "propertyTitlePosition"
        private const val PROPERTY_PRICE_POSITION = "propertyPricePosition"
        private const val PROPERTY_PERCENT_POSITION = "propertyPercentPosition"
        private const val PROPERTY_TEXT_ALPHA = "propertyTextAlpha"
    }
}