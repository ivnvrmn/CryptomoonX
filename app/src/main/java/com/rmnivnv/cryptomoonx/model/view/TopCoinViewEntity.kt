package com.rmnivnv.cryptomoonx.model.view

data class TopCoinViewEntity(
    val title: String,
    val price: String,
    val changePercent: String,
    val logoUrl: String,
    val isPositive: Boolean,
    var isClicked: Boolean = false
)