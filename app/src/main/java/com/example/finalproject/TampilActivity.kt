package com.example.finalproject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class TampilActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var imageAdapter: ImageAdapter
    private lateinit var storageReference: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tampil)

        recyclerView = findViewById(R.id.recyclerViewImages)
        recyclerView.layoutManager = GridLayoutManager(this, 3) // Sesuaikan dengan jumlah kolom yang diinginkan

        val imageUrlList: MutableList<String> = mutableListOf()

        storageReference = FirebaseStorage.getInstance().reference.child("images")

        // Ganti path sesuai dengan lokasi gambar di Firebase Storage
        storageReference.listAll().addOnSuccessListener { result ->
            for (imageRef in result.items) {
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    val imageUrl = uri.toString()
                    imageUrlList.add(imageUrl)

                    // Setelah mendapatkan daftar URL gambar, inisialisasikan adapter RecyclerView
                    imageAdapter = ImageAdapter(imageUrlList)
                    recyclerView.adapter = imageAdapter
                }.addOnFailureListener { exception ->
                    // Handle jika gagal mendapatkan URL gambar
                }
            }
        }.addOnFailureListener {
            // Handle jika gagal mendapatkan daftar gambar dari Firebase Storage
        }
    }
}

