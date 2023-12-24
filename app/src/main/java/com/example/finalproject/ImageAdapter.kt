package com.example.finalproject

//import android.content.Context
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageView
//import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.Glide
//import com.squareup.picasso.Picasso

//class ImageAdapter(private val context: Context, private val imageList: List<String>) :
//    RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
//        val view = LayoutInflater.from(context).inflate(R.layout.item_image, parent, false)
//        return ImageViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
//        val imageUrl = imageList[position]
//        Picasso.get().load(imageUrl).into(holder.imageViewItem)
//    }
//
//
//    override fun getItemCount(): Int {
//        return imageList.size
//    }
//
//    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val imageViewItem: ImageView = itemView.findViewById(R.id.imageViewItem)
//    }
//}

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class ImageAdapter(private val imageUrlList: List<String>) :
    RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imageUrl = imageUrlList[position]
        holder.bind(imageUrl)
    }

    override fun getItemCount(): Int {
        return imageUrlList.size
    }

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.imageViewItem)

        fun bind(imageUrl: String) {
            Picasso.get().load(imageUrl).into(imageView)
        }
    }
}
