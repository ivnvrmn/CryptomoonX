package com.rmnivnv.cryptomoonx.coininfo

import android.app.Dialog
import android.view.View
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.rmnivnv.cryptomoonx.R

class CoinInfoFragment(
    private val name: String,
    private val price: String,
    private val onDismiss: () -> Unit,
    private val onSlide: (Float) -> Unit
) : BottomSheetDialogFragment() {

    private val bottomSheetBehavior = object : BottomSheetBehavior.BottomSheetCallback() {
        override fun onStateChanged(bottomSheet: View, newState: Int) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss()
            }
        }

        override fun onSlide(bottomSheet: View, slideOffset: Float) {
            onSlide(slideOffset)
        }
    }

    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        val contentView = View.inflate(context, R.layout.coin_info, null)
        contentView.findViewById<TextView>(R.id.coin_name).text = name
        contentView.findViewById<TextView>(R.id.coin_price).text = price
        dialog.setContentView(contentView)

        (contentView.parent as View).apply {
            setBackgroundColor(ContextCompat.getColor(context!!, android.R.color.transparent))
            (layoutParams as CoordinatorLayout.LayoutParams).behavior?.also {
                if (it is BottomSheetBehavior<*>) {
                    it.setBottomSheetCallback(bottomSheetBehavior)
                }
            }
        }
    }

    override fun onDestroy() {
        onDismiss()
        super.onDestroy()
    }
}