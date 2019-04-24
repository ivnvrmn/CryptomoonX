package com.rmnivnv.cryptomoonx.network

import com.rmnivnv.cryptomoonx.model.api.TopCoinsResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CryptocompareApi {

    @GET("top/totalvolfull")
    fun getTopCoins(
        @Query("limit") limit: Int = 100,
        @Query("tsym") toSymbol: String = "USD"
    ): Deferred<Response<TopCoinsResponse>>
}