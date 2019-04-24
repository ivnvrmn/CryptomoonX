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
import com.rmnivnv.cryptomoonx.model.view.TopCoinViewEntity
import com.rmnivnv.cryptomoonx.network.ApiFactory

class TopFragment : Fragment(), TopContract.View {

    private val presenter: TopContract.Presenter by lazy { createPresenter() }
    private lateinit var topAdapter: TopAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return context?.let {
            topAdapter = TopAdapter()
            val recyclerView = RecyclerView(it).apply {
                layoutManager = LinearLayoutManager(it, RecyclerView.VERTICAL, false)
                adapter = topAdapter
            }

            FrameLayout(it).apply {
                setBackgroundColor(ContextCompat.getColor(context, R.color.color_primary_dark))
                addView(recyclerView)
            }
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
    }

    private fun createPresenter(): TopContract.Presenter {
        return TopPresenter(this, TopRepository(ApiFactory.cryptoCompareApi))
    }
}