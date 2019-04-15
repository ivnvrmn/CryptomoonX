package com.rmnivnv.cryptomoonx.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.rmnivnv.cryptomoonx.R

class FavoritesFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val frameLayout = FrameLayout(context!!).apply {
            setBackgroundColor(ContextCompat.getColor(context, R.color.green))
        }
        return frameLayout
    }
}