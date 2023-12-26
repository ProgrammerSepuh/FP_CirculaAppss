package com.example.finalproject

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

//class ImageUserAdapter(private val context: Context, private val imageList: List<String>, private val imageDescriptionList: List<String>) :
//    RecyclerView.Adapter<ImageUserAdapter.ImageViewHolder>() {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
//        val view = LayoutInflater.from(context).inflate(R.layout.item_image_user, parent, false)
//        return ImageViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
//        val imageUrl = imageList[position]
//        val imageDescription = imageDescriptionList[position]
//        holder.bind(imageUrl, imageDescription)
//    }
//
//    override fun getItemCount(): Int {
//        return imageList.size
//    }
//
//    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        private val imageView: ImageView = itemView.findViewById(R.id.imageView)
//        private val imageDescriptionTextView: TextView = itemView.findViewById(R.id.deskripsi)
//
//        fun bind(imageUrl: String, imageDescription: String) {
//            // Load gambar menggunakan Glide atau library serupa
//            Glide.with(context)
//                .load(imageUrl)
//                .into(imageView)
//
//            // Set deskripsi gambar
//            imageDescriptionTextView.text = imageDescription
//        }
//    }
//}

class ImageUserAdapter(private val context: Context, private val imageList: List<String>, private val descriptionList: List<String>) :
    RecyclerView.Adapter<ImageUserAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_image_user, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imageUrl = imageList[position]
        val description = descriptionList[position]
        holder.bind(imageUrl, description)
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.imageView)
        private val textViewDescription: TextView = itemView.findViewById(R.id.deskripsi)

        fun bind(imageUrl: String, description: String) {
            // Load gambar menggunakan Glide atau library serupa
            Glide.with(context)
                .load(imageUrl)
                .into(imageView)

            // Set deskripsi gambar ke TextView
            textViewDescription.text = description

            // Tambahkan tindakan klik pada gambar di sini
            imageView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    // Ketika item gambar diklik, dapatkan URL dan deskripsi gambar
                    val imageUrl = imageList[position]
                    val description = descriptionList[position]

                    // Gunakan intent untuk menampilkan gambar dan deskripsi secara penuh di aktivitas baru
                    val intent = Intent(context, DetailImageActivity::class.java)
                    intent.putExtra("imageUrl", imageUrl)
                    intent.putExtra("imageDescription", description)
                    context.startActivity(intent)
                }
            }
        }
    }

}




