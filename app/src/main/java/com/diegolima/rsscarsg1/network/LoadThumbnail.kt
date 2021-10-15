package com.diegolima.rsscarsg1.network

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.diegolima.rsscarsg1.R

fun loadImage(mContext: Context, urlImg : String, img: ImageView){
    Glide.with(mContext)
        .load(urlImg)
        .error(R.drawable.ic_error)
        .into(img)
}