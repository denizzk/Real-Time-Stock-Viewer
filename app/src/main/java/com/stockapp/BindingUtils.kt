package com.stockapp

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.text.DecimalFormat

@BindingAdapter("setText")
fun TextView.setName(item: String) {
    item.let {
        text = item
    }
}

@BindingAdapter("setText")
fun TextView.setPrice(item: Double) {
    item.let {
        text = DecimalFormat("#,###.00 €").format(item).toString()
    }
}

@BindingAdapter("setTrendImage")
@Suppress("SENSELESS_COMPARISON")
fun ImageView.setTrendPics(item: String) {
    item.let {
        if (item.equals("up")) Glide.with(context).load(R.drawable.ic_trending_up)
            .apply(RequestOptions()).into(this)
        else Glide.with(context).load(R.drawable.ic_trending_down)
            .apply(RequestOptions()).into(this)
    }
}

@BindingAdapter("setOriginImage")
@Suppress("SENSELESS_COMPARISON")
fun ImageView.setOriginPics(item: String) {
    item.let {
       Glide.with(context).load("https:www.countryflags.io/"+item+"/flat/64.png")
            .apply(RequestOptions()).into(this)
    }
}