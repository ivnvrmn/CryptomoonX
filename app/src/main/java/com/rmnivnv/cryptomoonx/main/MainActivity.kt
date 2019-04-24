package com.rmnivnv.cryptomoonx.main

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.rmnivnv.cryptomoonx.favorites.FavoritesFragment
import com.rmnivnv.cryptomoonx.top.TopFragment
import com.rmnivnv.cryptomoonx.views.BottomNavigationView

private const val ID_BOTTOM_NAVIGATION = 1
private const val ID_CONTAINER = 2

private const val TAG_TOP = "tagTop"
private const val TAG_FAVORITES = "tagFavorites"

class MainActivity : AppCompatActivity(),
    BottomNavigationView.OnPositionChangeListener,
    MainContract.View {

    private val presenter: MainContract.Presenter by lazy { MainPresenter(this) }
    private val fragmentTop: Fragment by lazy { TopFragment() }
    private val fragmentFavorites: Fragment by lazy { FavoritesFragment() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(resolveView())
        presenter.onCreate()
    }

    private fun resolveView(): View {
        val mainLayout = RelativeLayout(this)
        val bottomNavigationView = BottomNavigationView(this).apply {
            layoutParams = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
            }
            id = ID_BOTTOM_NAVIGATION
            setListener(this@MainActivity)
        }
        mainLayout.addView(bottomNavigationView)
        val container = FrameLayout(this).apply {
            layoutParams = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
            ).apply {
                addRule(RelativeLayout.ABOVE, ID_BOTTOM_NAVIGATION)
            }
            id = ID_CONTAINER
        }
        mainLayout.addView(container)

        return mainLayout
    }

    override fun onPositionChanged(position: BottomNavigationView.Position) {
        presenter.onPositionChanged(position)
    }

    override fun showTop() {
        showFragment(fragmentTop, TAG_TOP, fragmentFavorites)
    }

    override fun showFavorites() {
        showFragment(fragmentFavorites, TAG_FAVORITES, fragmentTop)
    }

    override fun showNews() {
        //
    }

    override fun showSettings() {

    }

    private fun showFragment(fragmentToShow: Fragment, fragmentToShowTag: String, fragmentToHide: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            if (fragmentToShow.isAdded) {
                show(fragmentToShow)
            } else {
                add(ID_CONTAINER, fragmentToShow, fragmentToShowTag)
            }

            if (fragmentToHide.isAdded) {
                hide(fragmentToHide)
            }

            commit()
        }
    }
}
