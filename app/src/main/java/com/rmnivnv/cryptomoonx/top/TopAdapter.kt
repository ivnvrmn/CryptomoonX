package com.rmnivnv.cryptomoonx.top

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rmnivnv.cryptomoonx.extensions.dpToPx
import com.rmnivnv.cryptomoonx.model.view.TopCoinViewEntity
import com.rmnivnv.cryptomoonx.utils.CircleTransform
import com.rmnivnv.cryptomoonx.views.CoinListItemView
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target

class TopAdapter : RecyclerView.Adapter<TopAdapter.ViewHolder>() {

    var coins: List<TopCoinViewEntity> = listOf()
    var listener: (TopCoinViewEntity) -> Unit = {}

    private val circleTransform = CircleTransform()
    private var iconSize: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        iconSize = CoinListItemView.LOGO_SIZE_DP.dpToPx(parent.resources)
        return ViewHolder(CoinListItemView(parent.context))
    }

    override fun getItemCount() = coins.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(coins[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val coinListItemView: CoinListItemView = itemView as CoinListItemView
        private val target = object : Target {
            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
            }

            override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                //todo show errorStub
            }

            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                bitmap?.also {
                    coinListItemView.setLogo(it)
                }
            }
        }

        fun bind(coin: TopCoinViewEntity) = with(coin) {
            coinListItemView.setData(title, price, changePercent, isPositive, isClicked)
            coinListItemView.setOnClickListener {
                listener(this)
                coinListItemView.onClicked()
            }

            Picasso.get()
                .load(coin.logoUrl)
                .resize(iconSize, iconSize)
                .transform(circleTransform)
                .into(target)
        }
    }
}