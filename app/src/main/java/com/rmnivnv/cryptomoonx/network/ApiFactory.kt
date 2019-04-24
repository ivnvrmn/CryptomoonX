package com.rmnivnv.cryptomoonx.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_CRYPTO_COMPARE_URL = "https://min-api.cryptocompare.com/data/"

object ApiFactory {

    val cryptoCompareApi: CryptocompareApi = createRetrofit().create(CryptocompareApi::class.java)

    private fun createRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_CRYPTO_COMPARE_URL)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}