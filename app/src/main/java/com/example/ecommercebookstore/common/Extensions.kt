package com.example.ecommercebookstore.common

import android.content.Context
import android.graphics.Paint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.ecommercebookstore.R

fun ImageView.loadImage(url: String?) {
    Glide.with(this.context).load(url).into(this)
}

fun View.gone(){
    visibility=View.GONE
}
fun View.visible(){
    visibility=View.VISIBLE
}
fun ImageView.downloadFromUrl(url:String,progressDrawable: CircularProgressDrawable){

    val options = RequestOptions()
        .placeholder(progressDrawable) //the drawable that is shown while data are loading
        .error(R.mipmap.ic_launcher_round)

    Glide.with(context)
        .setDefaultRequestOptions(options)
        .load(url)
        .into(this)
}

fun placeHolderProgressBar(context: Context):CircularProgressDrawable{
    return CircularProgressDrawable(context).apply {
        strokeWidth=8f
        centerRadius=40f
        start()
    }

}
fun TextView.setStrikeThrough() {
    paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
}
