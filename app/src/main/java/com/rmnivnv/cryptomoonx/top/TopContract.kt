package com.rmnivnv.cryptomoonx.top

import androidx.recyclerview.widget.DiffUtil
import com.rmnivnv.cryptomoonx.model.view.TopCoinViewEntity

interface TopContract {

    interface View {
        fun showCoins(newCoins: List<TopCoinViewEntity>, diffResult: DiffUtil.DiffResult)
        fun showRefreshAnimation()
        fun hideRefreshAnimation()
        fun updateTime()
        fun showCoinInfo(coin: TopCoinViewEntity)
    }

    interface Presenter {
        var view: View
        fun onViewCreated()
        fun onDestroy()
        fun onRefresh()
        fun onCoinClicked(coin: TopCoinViewEntity)
    }

    interface Repository {
        suspend fun getTopCoins(): List<TopCoinViewEntity>?
    }
}