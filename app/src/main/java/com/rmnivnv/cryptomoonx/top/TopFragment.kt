package com.rmnivnv.cryptomoonx.top

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rmnivnv.cryptomoonx.R
import com.rmnivnv.cryptomoonx.mockTopList

class TopFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        context?.also {
            val recyclerView = RecyclerView(it).apply {
                layoutManager = LinearLayoutManager(it, RecyclerView.VERTICAL, false)
            }
            val adapter = TopAdapter().apply {
                coins = mockTopList
                listener = {

                }
            }
            recyclerView.adapter = adapter

            val frameLayout = FrameLayout(it).apply {
                setBackgroundColor(ContextCompat.getColor(context, R.color.color_primary_dark))
            }

            frameLayout.addView(recyclerView)

            return frameLayout
        }
        return null
    }
}