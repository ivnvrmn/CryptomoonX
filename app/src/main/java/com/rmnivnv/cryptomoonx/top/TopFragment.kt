package com.rmnivnv.cryptomoonx.top

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.rmnivnv.cryptomoonx.R
import com.rmnivnv.cryptomoonx.views.CoinListItemView

class TopFragment : Fragment() {

    private lateinit var coin: CoinListItemView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        context?.also {
            coin = CoinListItemView(it)
            val frameLayout = FrameLayout(it).apply {
                setBackgroundColor(ContextCompat.getColor(context, R.color.color_primary_dark))
            }
            frameLayout.addView(coin)

            return frameLayout
        }
        return null
    }
}