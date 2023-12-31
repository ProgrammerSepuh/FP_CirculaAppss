package com.example.finalproject

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
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
        recyclerView.layoutManager = GridLayoutManager(this, 3)

        val imageUrlList: MutableList<String> = mutableListOf()

        storageReference = FirebaseStorage.getInstance().reference.child("images")


        storageReference.listAll().addOnSuccessListener { result ->
            for (imageRef in result.items) {
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    val imageUrl = uri.toString()
                    imageUrlList.add(imageUrl)


                    imageAdapter = ImageAdapter(imageUrlList)
                    recyclerView.adapter = imageAdapter
                }.addOnFailureListener { exception ->

                }
            }
        }.addOnFailureListener {

        }

        val btnPost : ImageView = findViewById(R.id.btnUpload)
        btnPost.setOnClickListener{
            val intupload = Intent(this, UploadActivity::class.java)
            startActivity(intupload)
        }

        val btnEx: ImageView = findViewById(R.id.explore)
        btnEx.setOnClickListener{

        }

        val btnSe: ImageView = findViewById(R.id.btnSearch)
        btnSe.setOnClickListener{
            val intea = Intent(this, SearchActivity::class.java)
            startActivity(intea)
        }

        val btnHom: ImageView = findViewById(R.id.home)
        btnHom.setOnClickListener{
            val intttt = Intent(this, HomeActivity::class.java)
            startActivity(intttt)
        }

        val btnPro: ImageView = findViewById(R.id.btnProfile)
        btnPro.setOnClickListener {
            val intentUpload = Intent(this, ProfileActivity::class.java)
            startActivity(intentUpload)
        }
    }
}

