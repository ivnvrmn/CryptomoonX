package com.rmnivnv.cryptomoonx.model.api

import com.google.gson.annotations.SerializedName

data class TopCoinsResponse(
    @SerializedName("Message")
    val message: String,
    @SerializedName("Type")
    val type: Int,
    @SerializedName("SponsoredData")
    val sponsoredData: List<TopCoinEntity>,
    @SerializedName("Data")
    val data: List<TopCoinEntity>,
    @SerializedName("HasWarning")
    val hasWarning: Boolean
)