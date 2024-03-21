package com.maddy.jetpackbookreader.common.model

data class Offer(
    val finskyOfferType: Int,
    val listPrice: ListPriceX,
    val retailPrice: RetailPrice
)