package com.abdullah.githubusers.extenstion

import android.view.View
import android.widget.ImageView
import com.abdullah.githubusers.R
import com.bumptech.glide.Glide

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun ImageView.loadImageByUrl(url: String?) {
    Glide.with(this)
        .load(url)
        .error(R.drawable.ic_user)
        .dontAnimate()
        .into(this)
}