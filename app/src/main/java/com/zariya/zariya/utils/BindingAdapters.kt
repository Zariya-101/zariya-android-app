package com.zariya.zariya.utils

import android.net.Uri
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("showFeedFromUrl")
fun ImageView.showFeedFromUrl(url: String?) {

}

@BindingAdapter("showFeedFromDrawable")
fun ImageView.showFeedFromDrawable(drawable: Int) {
    Glide.with(this.context)
        .load(drawable)
        .into(this)
}

@BindingAdapter("loadImageFromUrl")
fun ImageView.loadImageFromUrl(url: String?) {
    Glide.with(this)
        .load(url)
        .into(this)
}