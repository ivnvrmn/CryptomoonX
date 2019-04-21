package com.rmnivnv.cryptomoonx.coininfo

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.FrameLayout
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.rmnivnv.cryptomoonx.R
import com.rmnivnv.cryptomoonx.extensions.dpToPx

class CoinInfoFragment : Fragment() {

    private lateinit var slideView: ViewGroup

    private var slideValue = 0
    private val slideViewAnimator = ValueAnimator().apply {
        interpolator = AccelerateDecelerateInterpolator()
        duration = 500
        addUpdateListener { animation ->
            slideValue = animation.animatedValue as Int

            slideView.layoutParams = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                slideValue
            ).apply {
                addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        context?.also {
            val mainLayout = RelativeLayout(it).apply {
                isClickable = true

                setOnTouchListener { v, event ->
                    val y = event.y
                    when (event.actionMasked) {
                        MotionEvent.ACTION_MOVE -> {
                            slideView.layoutParams = RelativeLayout.LayoutParams(
                                RelativeLayout.LayoutParams.MATCH_PARENT,
                                v.height - y.toInt()
                            ).apply {
                                addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
                            }
                        }
                    }


                    true
                }
            }
            slideView = FrameLayout(it).apply {
                layoutParams = RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    0
                ).apply {
                    addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
                }
                setBackgroundColor(ContextCompat.getColor(context, R.color.green))
            }
            mainLayout.addView(slideView)
            return mainLayout
        }

        return null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        slideViewAnimator.setIntValues(0, 400f.dpToPx(resources))
        slideViewAnimator.start()
    }
}