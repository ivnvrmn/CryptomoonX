package com.rmnivnv.cryptomoonx.top

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rmnivnv.cryptomoonx.extensions.dpToPx
import com.rmnivnv.cryptomoonx.model.Preferences
import com.rmnivnv.cryptomoonx.model.view.TopCoinViewEntity
import com.rmnivnv.cryptomoonx.utils.CircleTransform
import com.rmnivnv.cryptomoonx.views.CoinListItemView
import com.rmnivnv.cryptomoonx.views.UpdateTimeListItemView
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target

private const val TYPE_UPDATED_TIME = 0
private const val TYPE_COIN = 1

class TopAdapter(
    private val preferences: Preferences
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var coins: List<TopCoinViewEntity> = listOf()
    var listener: (TopCoinViewEntity) -> Unit = {}

    private val circleTransform = CircleTransform()
    private var iconSize: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        iconSize = CoinListItemView.LOGO_SIZE_DP.dpToPx(parent.resources)
        return when (viewType) {
            TYPE_UPDATED_TIME -> ViewHolderUpdatedTime(UpdateTimeListItemView(parent.context))
            else -> ViewHolderCoin(CoinListItemView(parent.context))
        }
    }

    override fun getItemCount() = if (coins.isNotEmpty()) coins.size + 1 else 0

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> TYPE_UPDATED_TIME
            else -> TYPE_COIN
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            TYPE_UPDATED_TIME -> (holder as ViewHolderUpdatedTime).bind()
            else -> (holder as ViewHolderCoin).bind(coins[position - 1])
        }
    }

    inner class ViewHolderCoin(itemView: View) : RecyclerView.ViewHolder(itemView) {
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
            coinListItemView.apply {
                setData(title, price, changePercent, isPositive, isClicked)
                setOnClickListener {
                    listener(coin)
                    coinListItemView.onClicked()
                }
                setLogo(null)
            }

            Picasso.get()
                .load(coin.logoUrl)
                .resize(iconSize, iconSize)
                .transform(circleTransform)
                .into(target)
        }
    }

    inner class ViewHolderUpdatedTime(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val updateTimeListItemView: UpdateTimeListItemView = itemView as UpdateTimeListItemView
        fun bind() {
            updateTimeListItemView.setUpdatedTime(preferences.topCoinsUpdatedTime)
        }
    }
}