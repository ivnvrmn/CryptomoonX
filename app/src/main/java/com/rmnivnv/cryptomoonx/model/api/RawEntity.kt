package com.rmnivnv.cryptomoonx.model.api

import com.google.gson.annotations.SerializedName

data class RawEntity(
    @SerializedName("USD")
    val usd: RawCoinEntity
)