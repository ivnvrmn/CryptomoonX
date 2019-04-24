package com.rmnivnv.cryptomoonx.top

import com.rmnivnv.cryptomoonx.CRYPTO_COMPARE_IMAGE_URL
import com.rmnivnv.cryptomoonx.model.api.TopCoinEntity
import com.rmnivnv.cryptomoonx.model.view.TopCoinViewEntity
import com.rmnivnv.cryptomoonx.network.CryptocompareApi

class TopRepository(
    private val cryptocompareApi: CryptocompareApi
): TopContract.Repository {

    override suspend fun getTopCoins(): List<TopCoinViewEntity>? {
        val response = cryptocompareApi.getTopCoins().await()
        return if (response.isSuccessful) {
            response.body()?.data?.map { it.toViewEntity() }
        } else {
            null
        }
    }

    private fun TopCoinEntity.toViewEntity(): TopCoinViewEntity {
        val title = "${coinInfo.fullName} (${coinInfo.name})"
        val price = display.usd.price
        val changePercent = "${display.usd.changePercent24Hour}%"
        val logoUrl = "$CRYPTO_COMPARE_IMAGE_URL${coinInfo.imageUrl}"
        val isPositive = raw.usd.changePct24Hour >= 0

        return TopCoinViewEntity(title, price, changePercent, logoUrl, isPositive)
    }
}