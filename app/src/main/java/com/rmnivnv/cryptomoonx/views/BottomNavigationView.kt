package com.rmnivnv.cryptomoonx.views

import android.animation.AnimatorSet
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import androidx.core.animation.doOnEnd
import androidx.core.graphics.ColorUtils
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.rmnivnv.cryptomoonx.COLOR_PRIMARY
import com.rmnivnv.cryptomoonx.COLOR_WHITE
import com.rmnivnv.cryptomoonx.R
import com.rmnivnv.cryptomoonx.extensions.dpToPx

private const val VIEW_HEIGHT_DP = 56f
private const val ICON_SIZE_DP = 24f
private const val ICON_SIZE_SELECTED_DP = 32f
private const val UNSELECTED_ICON_ALPHA = 100
private const val SELECTED_ICON_ALPHA = 255
private const val ICON_CLICKED_ANIMATION_DURATION = 100L

private const val PROPERTY_ALPHA_UP = "propertyAlphaUp"
private const val PROPERTY_ALPHA_DOWN = "propertyAlphaDown"
private const val PROPERTY_CLICKED_ICON_SIZE = "propertyClickedIconSize"
private const val PROPERTY_DESELECTED_ICON_SIZE = "propertyDeselectedIconSize"

private const val ICON_NOT_SELECTED = -1
private const val ICON_TOP_100 = 0
private const val ICON_FAVORITES = 1
private const val ICON_NEWS = 2
private const val ICON_SETTINGS = 3

class BottomNavigationView
@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr)  {

    private lateinit var listener: OnPositionChangeListener

    private val iconTop100 = VectorDrawableCompat.create(resources, R.drawable.ic_trending_up_white_24dp, null)
    private val iconFavorite = VectorDrawableCompat.create(resources, R.drawable.ic_star_white_24dp, null)
    private val iconNews = VectorDrawableCompat.create(resources, R.drawable.ic_message_white_24dp, null)
    private val iconSettings = VectorDrawableCompat.create(resources, R.drawable.ic_settings_white_24dp, null)

    private val colorFilterSelectedIcon = PorterDuffColorFilter(COLOR_WHITE, PorterDuff.Mode.SRC_IN)
    private val colorFilterUnselectedIcon = PorterDuffColorFilter(
        ColorUtils.setAlphaComponent(COLOR_WHITE, UNSELECTED_ICON_ALPHA),
        PorterDuff.Mode.SRC_IN
    )

    private val iconSize = ICON_SIZE_DP.dpToPx(resources)
    private val halfIconSize = iconSize / 2
    private val selectedIconSize = ICON_SIZE_SELECTED_DP.dpToPx(resources)
    private val iconIncreasedSize = selectedIconSize + (selectedIconSize / 3)
    private val halfSelectedIconSize = selectedIconSize / 2
    private var quarterWidth = 0
    private var middleVertical = 0

    private var iconSelected = ICON_TOP_100
    private var iconClicked = ICON_NOT_SELECTED
    private var iconDeselected = ICON_NOT_SELECTED
    private var lastClickedIcon = ICON_TOP_100
    private var currentPosition = Position.TOP

    private var iconsAppearValue = 0
    private var iconClickedSizeValue = iconSize
    private var iconDeselectedSizeValue = selectedIconSize
    private var alphaUpValue = UNSELECTED_ICON_ALPHA
    private var alphaDownValue = SELECTED_ICON_ALPHA

    private val alphaUpHolder = PropertyValuesHolder.ofInt(
        PROPERTY_ALPHA_UP, UNSELECTED_ICON_ALPHA, SELECTED_ICON_ALPHA
    )
    private val alphaDownHolder = PropertyValuesHolder.ofInt(
        PROPERTY_ALPHA_DOWN, SELECTED_ICON_ALPHA, UNSELECTED_ICON_ALPHA
    )
    private val clickedIconSizeHolder = PropertyValuesHolder.ofInt(
        PROPERTY_CLICKED_ICON_SIZE, iconIncreasedSize, selectedIconSize
    )
    private val deselectedIconSizeHolder = PropertyValuesHolder.ofInt(
        PROPERTY_DESELECTED_ICON_SIZE, selectedIconSize, iconSize
    )

    private val iconsAppearAnimator = ValueAnimator().apply {
        interpolator = AccelerateDecelerateInterpolator()
        addUpdateListener { animation ->
            iconsAppearValue = animation.animatedValue as Int
            invalidate()
        }
    }
    private val iconIncreaseAnimator = ValueAnimator.ofInt(iconSize, iconIncreasedSize).apply {
        interpolator = AccelerateInterpolator()
        addUpdateListener { animation ->
            iconClickedSizeValue = animation.animatedValue as Int
            invalidate()
        }
    }
    private val iconDecreaseAnimator = ValueAnimator().apply {
        setValues(alphaUpHolder, clickedIconSizeHolder)
        interpolator = AccelerateDecelerateInterpolator()
        addUpdateListener { animation ->
            iconClickedSizeValue = animation.getAnimatedValue(PROPERTY_CLICKED_ICON_SIZE) as Int
            alphaUpValue = animation.getAnimatedValue(PROPERTY_ALPHA_UP) as Int
            invalidate()
        }
    }

    private val iconClickedAnimatorSet = AnimatorSet().apply {
        playSequentially(iconIncreaseAnimator, iconDecreaseAnimator)
        duration = ICON_CLICKED_ANIMATION_DURATION
        doOnEnd {
            lastClickedIcon = iconClicked
            iconClicked = ICON_NOT_SELECTED
            alphaUpValue = UNSELECTED_ICON_ALPHA
        }
    }

    private val iconDeselectedAnimator = ValueAnimator().apply {
        interpolator = AccelerateDecelerateInterpolator()
        duration = ICON_CLICKED_ANIMATION_DURATION * 2
        setValues(alphaDownHolder, deselectedIconSizeHolder)
        addUpdateListener { animation ->
            alphaDownValue = animation.getAnimatedValue(PROPERTY_ALPHA_DOWN) as Int
            iconDeselectedSizeValue = animation.getAnimatedValue(PROPERTY_DESELECTED_ICON_SIZE) as Int
        }
        doOnEnd {
            iconDeselected = ICON_NOT_SELECTED
            alphaDownValue = SELECTED_ICON_ALPHA
        }
    }

    init {
        setBackgroundColor(COLOR_PRIMARY)
    }

    fun setListener(listener: OnPositionChangeListener) {
        this.listener = listener
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = VIEW_HEIGHT_DP.dpToPx(resources)
        setMeasuredDimension(width, height)
        quarterWidth = width / 4
        middleVertical = height / 2

        iconsAppearAnimator.apply {
            val startAppearValue = height - (height - middleVertical - halfIconSize)
            setIntValues(startAppearValue, 0)
            start()
        }
    }

    override fun onDraw(canvas: Canvas) {
        drawIcons(canvas)
    }

    private fun drawIcons(canvas: Canvas) {
        drawTop100Icon(canvas)
        drawFavoritesIcon(canvas)
        drawNewsIcon(canvas)
        drawSettingsIcon(canvas)
    }

    private fun drawTop100Icon(canvas: Canvas) {
        val middleHorizontal = quarterWidth / 2
        val halfIcon = getHalfIconSize(ICON_TOP_100)
        drawIcon(
            canvas,
            middleHorizontal,
            iconTop100,
            halfIcon,
            ICON_TOP_100
        )
    }

    private fun drawFavoritesIcon(canvas: Canvas) {
        val middleHorizontal = quarterWidth / 2 + quarterWidth
        val halfIcon = getHalfIconSize(ICON_FAVORITES)
        drawIcon(
            canvas,
            middleHorizontal,
            iconFavorite,
            halfIcon,
            ICON_FAVORITES
        )
    }

    private fun drawNewsIcon(canvas: Canvas) {
        val middleHorizontal = quarterWidth / 2 + (quarterWidth * 2)
        val halfIcon = getHalfIconSize(ICON_NEWS)
        drawIcon(
            canvas,
            middleHorizontal,
            iconNews,
            halfIcon,
            ICON_NEWS
        )
    }

    private fun drawSettingsIcon(canvas: Canvas) {
        val middleHorizontal = quarterWidth / 2 + (quarterWidth * 3)
        val halfIcon = getHalfIconSize(ICON_SETTINGS)
        drawIcon(
            canvas,
            middleHorizontal,
            iconSettings,
            halfIcon,
            ICON_SETTINGS
        )
    }

    private fun getHalfIconSize(icon: Int): Int {
        return when (icon) {
            iconClicked -> iconClickedSizeValue / 2
            iconSelected -> halfSelectedIconSize
            iconDeselected -> iconDeselectedSizeValue / 2
            else -> halfIconSize
        }
    }

    private fun drawIcon(
        canvas: Canvas,
        middleHorizontal: Int,
        icon: VectorDrawableCompat?,
        halfIcon: Int,
        iconId: Int
    ) {
        val isClicked = iconClicked == iconId
        val isSelected = iconSelected == iconId
        val isDeselected = iconDeselected == iconId
        val left = middleHorizontal - halfIcon
        val top = middleVertical - halfIcon + iconsAppearValue
        val right = middleHorizontal + halfIcon
        val bottom = middleVertical + halfIcon + iconsAppearValue
        icon?.apply {
            icon.colorFilter = when {
                isClicked -> {
                    val alpha = if (alphaUpValue != 0) alphaUpValue else UNSELECTED_ICON_ALPHA
                    PorterDuffColorFilter(
                        ColorUtils.setAlphaComponent(COLOR_WHITE, alpha),
                        PorterDuff.Mode.SRC_IN
                    )
                }
                isSelected -> colorFilterSelectedIcon
                isDeselected -> PorterDuffColorFilter(
                    ColorUtils.setAlphaComponent(COLOR_WHITE, alphaDownValue),
                    PorterDuff.Mode.SRC_IN
                )
                else -> colorFilterUnselectedIcon
            }
            setBounds(left, top, right, bottom)
            draw(canvas)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (x >= 0 && x < quarterWidth) {
                    setPosition(Position.TOP)
                    onIconClicked(ICON_TOP_100)
                } else if (x >= quarterWidth && x < (quarterWidth * 2)) {
                    setPosition(Position.FAVORITE)
                    onIconClicked(ICON_FAVORITES)
                } else if (x >= (quarterWidth * 2) && x < ((quarterWidth * 3))) {
                    setPosition(Position.NEWS)
                    onIconClicked(ICON_NEWS)
                } else {
                    setPosition(Position.SETTINGS)
                    onIconClicked(ICON_SETTINGS)
                }
            }
        }

        return true
    }

    private fun setPosition(position: Position) {
        if (currentPosition != position) {
            currentPosition = position
            listener.onPositionChanged(position)
        }
    }

    private fun onIconClicked(icon: Int) {
        if (lastClickedIcon != ICON_NOT_SELECTED && iconClicked != icon) {
            iconDeselected = lastClickedIcon
            iconDeselectedAnimator.start()
        }
        iconClicked = icon
        iconSelected = icon
        iconClickedAnimatorSet.start()
    }

    interface OnPositionChangeListener {
        fun onPositionChanged(position: Position)
    }

    enum class Position {
        TOP, FAVORITE, NEWS, SETTINGS
    }
}