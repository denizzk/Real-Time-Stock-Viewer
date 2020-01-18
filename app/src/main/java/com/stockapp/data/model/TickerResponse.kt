package com.stockapp.data.model

import com.squareup.moshi.Json

data class TickerResponse(
    var origin: String,
    @Json(name = "isin") val isin: String,
    var trendRatio: Double,
    var trend: String,
    @Json(name = "price") val price: Double
)