package com.rmnivnv.cryptomoonx.main

import com.rmnivnv.cryptomoonx.views.BottomNavigationView

class MainPresenter(
    override var view: MainContract.View?
) : MainContract.Presenter {

    override fun onCreate() {
        view?.showTop()
    }

    override fun onPositionChanged(position: BottomNavigationView.Position) {
        when (position) {
            BottomNavigationView.Position.TOP -> view?.showTop()
            BottomNavigationView.Position.FAVORITE -> view?.showFavorites()
            BottomNavigationView.Position.NEWS -> view?.showNews()
            BottomNavigationView.Position.SETTINGS -> view?.showSettings()
        }
    }
}