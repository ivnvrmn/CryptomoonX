package com.rmnivnv.cryptomoonx.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_CRYPTO_COMPARE_URL = "https://min-api.cryptocompare.com/data/"

object ApiFactory {

    val cryptoCompareApi: CryptocompareApi = createRetrofit().create(CryptocompareApi::class.java)

    private fun createRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_CRYPTO_COMPARE_URL)
            .client(createClient())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun createClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
        return OkHttpClient.Builder().addInterceptor(interceptor).build()
    }
}