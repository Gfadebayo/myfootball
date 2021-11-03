package com.exzell.myfootball.utils

import android.widget.ImageView
import com.bumptech.glide.Glide

fun ImageView.loadImage(path: String?){
    if(path == null || path.isEmpty()) return

    Glide.with(this)
            .load(path)
            .into(this)
            .request!!.apply {
                if(!isRunning) begin()
            }
}