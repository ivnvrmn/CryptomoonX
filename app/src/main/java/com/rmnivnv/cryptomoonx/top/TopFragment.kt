package com.rmnivnv.cryptomoonx.top

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.rmnivnv.cryptomoonx.R
import com.rmnivnv.cryptomoonx.model.PreferencesImpl
import com.rmnivnv.cryptomoonx.model.view.TopCoinViewEntity
import com.rmnivnv.cryptomoonx.network.ApiFactory

class TopFragment : Fragment(), TopContract.View {

    private val presenter: TopContract.Presenter by lazy {
        TopPresenter(
            this,
            TopRepository(ApiFactory.cryptoCompareApi),
            PreferencesImpl(context!!)
        ) { System.currentTimeMillis() }
    }
    private lateinit var refreshLayout: SwipeRefreshLayout
    private lateinit var topAdapter: TopAdapter
    private lateinit var recyclerView: RecyclerView
    private var isInitStart = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return context?.let {
            initRecyclerView(it)
            SwipeRefreshLayout(it).apply {
                setBackgroundColor(ContextCompat.getColor(it, R.color.color_primary_dark))
                setProgressBackgroundColorSchemeResource(R.color.color_primary)
                setColorSchemeColors(
                    ContextCompat.getColor(it, R.color.white),
                    ContextCompat.getColor(it, R.color.green),
                    ContextCompat.getColor(it, R.color.red)
                )

                setOnRefreshListener { presenter.onRefresh() }

                addView(recyclerView)
                refreshLayout = this
            }
        }
    }

    private fun initRecyclerView(context: Context) {
        topAdapter = TopAdapter(PreferencesImpl(context))
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

    override fun showCoins(newCoins: List<TopCoinViewEntity>, diffResult: DiffUtil.DiffResult) {
        topAdapter.coins = newCoins
        diffResult.dispatchUpdatesTo(topAdapter)

        if (isInitStart) {
            isInitStart = false
            recyclerView.scheduleLayoutAnimation()
        }
    }

    override fun updateTime() {
        topAdapter.notifyItemChanged(0)
    }

    override fun showRefreshAnimation() {
        refreshLayout.isRefreshing = true
    }

    override fun hideRefreshAnimation() {
        refreshLayout.isRefreshing = false
    }
}