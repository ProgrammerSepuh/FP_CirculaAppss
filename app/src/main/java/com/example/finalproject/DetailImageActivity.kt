package com.example.finalproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
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
    private lateinit var imageUrl: String // Variabel untuk menyimpan imageUrl
    private lateinit var btn :Button
    private lateinit var btnDelete: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_image)

        imageViewDetail = findViewById(R.id.imageViewDetail)
        textViewDescription = findViewById(R.id.textViewDescription)
        btn = findViewById(R.id.buttonSave)
        imageUrl = intent.getStringExtra("imageUrl") ?: ""
        val description = intent.getStringExtra("imageDescription") ?: ""

        // Tampilkan gambar pada ImageView secara penuh
        Glide.with(this)
            .load(imageUrl)
            .into(imageViewDetail)

        // Tampilkan deskripsi gambar pada TextView
        textViewDescription.text = description

        val btnSave: Button = findViewById(R.id.buttonSave)
        btnSave.setOnClickListener {
            val newDescription = findViewById<EditText>(R.id.textViewDescription).text.toString()
            saveImageDescriptionToFirebase(newDescription)
        }
        btnDelete = findViewById(R.id.btnDelete)
        btnDelete.setOnClickListener{
            deleteImageFromFirebase()
        }

    }

    private fun saveImageDescriptionToFirebase(newDescription: String) {
        // Simpan deskripsi gambar yang baru ke Firebase
        val databaseReference = FirebaseDatabase.getInstance().reference.child("uploads")
        val query = databaseReference.orderByChild("imageUrl").equalTo(imageUrl)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    // Ubah deskripsi gambar
                    snapshot.ref.child("imageDescription").setValue(newDescription)
                        .addOnSuccessListener {
                            Toast.makeText(this@DetailImageActivity, "Image description updated.", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this@DetailImageActivity, "Failed to update image description.", Toast.LENGTH_SHORT).show()
                        }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error saat mengambil data dari Firebase
                Toast.makeText(this@DetailImageActivity, "Failed to update image description.", Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun deleteImageFromFirebase() {
        val databaseReference = FirebaseDatabase.getInstance().reference.child("uploads")
        val query = databaseReference.orderByChild("imageUrl").equalTo(imageUrl)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    snapshot.ref.removeValue()
                        .addOnSuccessListener {
                            Toast.makeText(this@DetailImageActivity, "Image deleted.", Toast.LENGTH_SHORT).show()
                            finish() // Kembali ke Activity sebelumnya setelah menghapus
                        }
                        .addOnFailureListener {
                            Toast.makeText(this@DetailImageActivity, "Failed to delete image.", Toast.LENGTH_SHORT).show()
                        }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error saat mengambil data dari Firebase
                Toast.makeText(this@DetailImageActivity, "Failed to delete image.", Toast.LENGTH_SHORT).show()
            }
        })
    }
}




