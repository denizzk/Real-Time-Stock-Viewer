package com.stockapp.data.model

import com.squareup.moshi.Json

data class TickerResponse(
    @Json(name = "isin") val isin: String,
    @Json(name = "price") val price: Double
)