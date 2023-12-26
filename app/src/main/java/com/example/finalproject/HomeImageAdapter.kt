package com.example.finalproject

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.finalproject.model.Upload
import com.example.finalproject.model.User

//class HomeImageAdapter(
//    private val context: Context,
//    private val imageList: MutableList<Upload>
//) : RecyclerView.Adapter<HomeImageAdapter.ImageViewHolder>() {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
//        val view = LayoutInflater.from(context).inflate(R.layout.item_home_image, parent, false)
//        return ImageViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
//        val upload = imageList[position]
//        holder.bind(upload)
//    }
//
//    override fun getItemCount(): Int {
//        return imageList.size
//    }
//
//    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        private val imageView: ImageView = itemView.findViewById(R.id.imageViewHome)
//        private val usernameTextView: TextView = itemView.findViewById(R.id.textViewUsernameHome)
//
//        fun bind(upload: Upload) {
//            Glide.with(context)
//                .load(upload.imageUrl)
//                .into(imageView)
//
//            usernameTextView.text = "@${upload.username}"
//        }
//    }
//}

class HomeImageAdapter(private val context: Context, private val imageList: List<String>,private val usernameList: List<String>) :
    RecyclerView.Adapter<HomeImageAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_home_image, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val imageUrl = imageList[position]
        val username = usernameList[position]
        holder.bind(imageUrl, username)
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageViewUser: ImageView = itemView.findViewById(R.id.imageViewUser)
        private val textViewUsername: TextView = itemView.findViewById(R.id.textViewUsername)


        fun bind(imageUrl: String, username: String) {

            Glide.with(context)
                .load(imageUrl)
                .into(imageViewUser)

            // Menampilkan informasi user ke TextView
            textViewUsername.text = "@$username"

        }
    }
}




