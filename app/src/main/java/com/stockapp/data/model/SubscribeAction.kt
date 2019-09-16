package com.stockapp.data.model

import com.squareup.moshi.Json

data class Subscribe(
    @Json(name = "subscribe") val subscribe: StockISIN
)
data class Unsubscribe (
    @Json(name = "unsubscribe") val unsubscribe: StockISIN
)

