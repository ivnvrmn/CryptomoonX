package com.rmnivnv.cryptomoonx.top

import com.rmnivnv.cryptomoonx.model.api.TopCoinEntity
import com.rmnivnv.cryptomoonx.model.view.TopCoinViewEntity
import com.rmnivnv.cryptomoonx.network.CryptocompareApi

private const val CRYPTO_COMPARE_IMAGE_URL = "https://www.cryptocompare.com/"

class TopRepository(
    private val cryptoCompareApi: CryptocompareApi
): TopContract.Repository {

    override suspend fun getTopCoins(): List<TopCoinViewEntity>? {
        return try {
            val response = cryptoCompareApi.getTopCoins().await()
            if (response.isSuccessful) {
                response.body()?.data?.map { it.toViewEntity() }
            } else {
                null
            }
        } catch (e: Exception) {
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