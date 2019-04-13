package com.rmnivnv.cryptomoonx.main

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import com.rmnivnv.cryptomoonx.COLOR_PRIMARY_DARK
import com.rmnivnv.cryptomoonx.views.BottomNavigationView

private const val ID_BOTTOM_NAVIGATION = 1
private const val ID_CONTAINER = 2

class MainActivity : AppCompatActivity(), BottomNavigationView.OnPositionChangeListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(resolveView())
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
            setBackgroundColor(COLOR_PRIMARY_DARK)
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
        Log.d("onPositionChanged", "position = ${position.name}")
    }
}
