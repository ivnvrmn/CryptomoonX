package com.rmnivnv.cryptomoonx.model.api

import com.google.gson.annotations.SerializedName

data class DisplayEntity(
    @SerializedName("USD")
    val usd: DisplayCoinEntity
)
