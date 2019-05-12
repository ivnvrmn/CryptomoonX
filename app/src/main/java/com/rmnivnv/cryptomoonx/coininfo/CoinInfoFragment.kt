package com.rmnivnv.cryptomoonx.coininfo

import android.app.Dialog
import android.view.View
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.rmnivnv.cryptomoonx.R

const val KEY_COIN_NAME = "coin_name"
const val KEY_COIN_PRICE = "coin_price"

class CoinInfoFragment : BottomSheetDialogFragment() {

    private lateinit var coinInfoListener: CoinInfoListener

    private val bottomSheetBehavior = object : BottomSheetBehavior.BottomSheetCallback() {
        override fun onStateChanged(bottomSheet: View, newState: Int) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss()
            }
        }

        override fun onSlide(bottomSheet: View, slideOffset: Float) {
            coinInfoListener.onSlide(slideOffset)
        }
    }

    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        val contentView = View.inflate(context, R.layout.coin_info, null).apply {
            findViewById<TextView>(R.id.coin_name).text = arguments?.getString(KEY_COIN_NAME)
            findViewById<TextView>(R.id.coin_price).text = arguments?.getString(KEY_COIN_PRICE)
        }
        dialog.setContentView(contentView)

        (contentView.parent as View).apply {
            setBackgroundColor(ContextCompat.getColor(context!!, android.R.color.transparent))
            (layoutParams as CoordinatorLayout.LayoutParams).behavior?.also {
                if (it is BottomSheetBehavior<*>) {
                    it.setBottomSheetCallback(bottomSheetBehavior)
                }
            }
        }

        coinInfoListener = parentFragment as CoinInfoListener
    }

    override fun onDestroy() {
        coinInfoListener.onDismiss()
        super.onDestroy()
    }
}