package com.rmnivnv.cryptomoonx.main

import com.rmnivnv.cryptomoonx.views.BottomNavigationView

interface MainContract {

    interface View {
        fun showTop()
        fun showFavorites()
        fun showNews()
        fun showSettings()
    }

    interface Presenter {
        var view: View?
        fun onCreate()
        fun onPositionChanged(position: BottomNavigationView.Position)
    }
}