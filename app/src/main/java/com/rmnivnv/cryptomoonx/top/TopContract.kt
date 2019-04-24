package com.rmnivnv.cryptomoonx.top

import com.rmnivnv.cryptomoonx.model.view.TopCoinViewEntity

interface TopContract {

    interface View {
        fun showCoins(newCoins: List<TopCoinViewEntity>)
    }

    interface Presenter {
        var view: View
        fun onViewCreated()
        fun onDestroy()
    }

    interface Repository {
        suspend fun getTopCoins(): List<TopCoinViewEntity>?
    }
}