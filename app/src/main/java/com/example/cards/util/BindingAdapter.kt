package com.example.cards.util

import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.cards.R

@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imageUrl: String?) {
    Glide.with(imgView.context)
        .load(imageUrl)
        .error(ContextCompat.getDrawable(imgView.context, R.drawable.tree))
        .into(imgView)
}