package com.rmnivnv.cryptomoonx.model.api

import com.google.gson.annotations.SerializedName

data class TopCoinEntity(
    @SerializedName("RAW")
    val raw: RawEntity,
    @SerializedName("DISPLAY")
    val display: DisplayEntity,
    @SerializedName("CoinInfo")
    val coinInfo: CoinInfo
)