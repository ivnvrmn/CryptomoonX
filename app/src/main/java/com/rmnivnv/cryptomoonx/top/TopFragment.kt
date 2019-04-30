package com.rmnivnv.cryptomoonx.top

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rmnivnv.cryptomoonx.R
import com.rmnivnv.cryptomoonx.model.view.TopCoinViewEntity
import com.rmnivnv.cryptomoonx.network.ApiFactory

class TopFragment : Fragment(), TopContract.View {

    private val presenter: TopContract.Presenter by lazy {
        TopPresenter(this, TopRepository(ApiFactory.cryptoCompareApi))
    }
    private lateinit var topAdapter: TopAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return context?.let {
            initRecyclerView(it)
            FrameLayout(it).apply {
                setBackgroundColor(ContextCompat.getColor(it, R.color.color_primary_dark))
                addView(recyclerView)
            }
        }
    }

    private fun initRecyclerView(context: Context) {
        topAdapter = TopAdapter()
        recyclerView = RecyclerView(context).apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = topAdapter
            layoutAnimation = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_from_bottom)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.onViewCreated()
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }

    override fun showCoins(newCoins: List<TopCoinViewEntity>) = with(topAdapter) {
        coins = newCoins
        notifyDataSetChanged()
        recyclerView.scheduleLayoutAnimation()
    }
}