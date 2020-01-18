package com.stockapp

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.stock_item_layout.view.*
import java.text.DecimalFormat
import kotlin.math.absoluteValue

@BindingAdapter("originImage")
@Suppress("SENSELESS_COMPARISON")
fun ImageView.setOriginImage(item: String) {
    item.let {
        Glide.with(context).load("https:www.countryflags.io/" + item + "/flat/64.png")
            .apply(RequestOptions()).into(this)
    }
}

@BindingAdapter("trendRatio")
fun TextView.setTrendRatio(item: Double) {
    item.let {
        if (item > 0){
            text = DecimalFormat("% 0.000 ").format(item).toString()
            stock_trend_ratio_text.setTextColor(resources.getColor(R.color.green))
        }
        else{
            text = DecimalFormat("% 0.000 ").format(item.absoluteValue).toString()
            stock_trend_ratio_text.setTextColor(resources.getColor(R.color.red))
        }
    }
}

@BindingAdapter("trendImage")
@Suppress("SENSELESS_COMPARISON")
fun ImageView.setTrendImage(item: String) {
    item.let {
        when (item) {
            "up" -> Glide.with(context).load(R.drawable.ic_trending_up)
                .apply(RequestOptions()).into(this)
            "down" -> Glide.with(context).load(R.drawable.ic_trending_down)
                .apply(RequestOptions()).into(this)
            else -> Glide.with(context).load(R.drawable.ic_trending_flat)
                .apply(RequestOptions()).into(this)
        }
    }
}

@BindingAdapter("price")
fun TextView.setPrice(item: Double) {
    item.let {
        text = DecimalFormat("#,###.00 €").format(item).toString()
    }
}

