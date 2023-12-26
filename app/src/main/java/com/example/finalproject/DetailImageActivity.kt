package com.example.finalproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso

//class DetailImageActivity : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_detail_image)
//        val imageUrl = intent.getStringExtra("imageUrl")
//
//        // Tampilkan gambar ke dalam ImageView di DetailImageActivity menggunakan Picasso atau Glide
//        val imageView: ImageView = findViewById(R.id.imageViewDetail)
//        Picasso.get().load(imageUrl).into(imageView)
//
//        // Jika Anda memiliki deskripsi untuk gambar, Anda dapat menampilkan deskripsi di sini
//        val imageDescription = "Deskripsi gambar" // Ganti dengan deskripsi gambar yang sebenarnya
//        val textViewDescription: TextView = findViewById(R.id.textViewDescription)
//        textViewDescription.text = imageDescription
//    }
//}

//class DetailImageActivity : AppCompatActivity() {
//
//    private lateinit var imageView: ImageView
//    private lateinit var textViewDescription: TextView
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_detail_image)
//
//        imageView = findViewById(R.id.imageViewDetail)
//        textViewDescription = findViewById(R.id.textViewDescription)
//
//        // Mendapatkan data gambar dan deskripsi dari intent
//        val imageUrl = intent.getStringExtra("imageUrl")
//        val imageDescription = intent.getStringExtra("imageDescription")
//
//        // Menampilkan gambar dan deskripsi pada layout
//        Glide.with(this)
//            .load(imageUrl)
//            .into(imageView)
//        textViewDescription.text = imageDescription
//    }
//}

class DetailImageActivity : AppCompatActivity() {

    private lateinit var imageViewDetail: ImageView
    private lateinit var textViewDescription: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_image)

        imageViewDetail = findViewById(R.id.imageViewDetail)
        textViewDescription = findViewById(R.id.textViewDescription)

        val imageUrl = intent.getStringExtra("imageUrl")
        val description = intent.getStringExtra("imageDescription")

        // Tampilkan gambar pada ImageView secara penuh
        Glide.with(this)
            .load(imageUrl)
            .into(imageViewDetail)

        // Tampilkan deskripsi gambar pada TextView
        textViewDescription.text = description
    }
}



