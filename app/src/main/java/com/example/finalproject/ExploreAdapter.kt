package com.example.finalproject
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.finalproject.model.Upload
// Ganti dengan path yang sesuai untuk file resources Anda

class ExploreAdapter(private val imageUrlList: MutableList<Upload>) :
    RecyclerView.Adapter<ExploreAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.item_image, parent, false)
        return ImageViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imageUrl = imageUrlList[position]
        holder.bind(imageUrl)
    }

    override fun getItemCount(): Int {
        return imageUrlList.size
    }

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageViewItemExplore: ImageView = itemView.findViewById(R.id.imageViewItemExplore)

        fun bind(imageUrl: Upload) {
            Glide.with(itemView.context)
                .load(imageUrl)
                .into(imageViewItemExplore)
        }
    }
}





