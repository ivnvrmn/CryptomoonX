package com.rmnivnv.cryptomoonx.top

import androidx.recyclerview.widget.DiffUtil
import com.rmnivnv.cryptomoonx.model.view.TopCoinViewEntity

class TopCoinsDiffUtilCallback(
    private val oldCoins: List<TopCoinViewEntity>,
    private val newCoins: List<TopCoinViewEntity>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldCoins.size

    override fun getNewListSize() = newCoins.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val old = oldCoins[oldItemPosition]
        val new = newCoins[newItemPosition]
        return old.title == new.title
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val old = oldCoins[oldItemPosition]
        val new = newCoins[newItemPosition]
        return old.title == new.title &&
                old.changePercent == new.changePercent &&
                old.logoUrl == new.logoUrl
    }
}