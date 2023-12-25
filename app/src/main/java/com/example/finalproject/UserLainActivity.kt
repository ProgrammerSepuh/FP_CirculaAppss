package com.example.finalproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UserLainActivity : AppCompatActivity() {
    private lateinit var database: FirebaseDatabase
    private lateinit var userId: String
    private lateinit var usernameTextView: TextView
    private lateinit var emailTextView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_lain)
        userId = intent.getStringExtra("userId") ?: ""

        usernameTextView = findViewById(R.id.textViewOtherUsername)
        emailTextView = findViewById(R.id.textViewOtherEmail)

        val databaseReference = FirebaseDatabase.getInstance().reference.child("users").child(userId)
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val username = dataSnapshot.child("username").getValue(String::class.java)
                val email = dataSnapshot.child("email").getValue(String::class.java)

                usernameTextView.text = username
                emailTextView.text = email
                fetchUserImages(userId)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error saat mengambil data dari Firebase
                Toast.makeText(this@UserLainActivity, "Failed to load user data.", Toast.LENGTH_SHORT).show()
            }
        })
    }
    // Di dalam UserLainActivity
    private fun fetchUserImages(uid: String) {
        val databaseReference = FirebaseDatabase.getInstance().reference.child("uploads")
        val query = databaseReference.orderByChild("s").equalTo(uid)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val imageList: MutableList<String> = mutableListOf()

                for (snapshot in dataSnapshot.children) {
                    val imageUrl = snapshot.child("imageUrl").getValue(String::class.java)
                    imageUrl?.let { imageList.add(it) }
                }

                // Setelah mendapatkan daftar URL gambar, pasang ke dalam RecyclerView
                val recyclerView: RecyclerView = findViewById(R.id.recyclerUserLain)
                recyclerView.layoutManager = GridLayoutManager(this@UserLainActivity, 3)
                val imageAdapter = UserLainAdapter(this@UserLainActivity, imageList)
                recyclerView.adapter = imageAdapter
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error saat mengambil data dari Firebase
                Toast.makeText(this@UserLainActivity, "Failed to load user images.", Toast.LENGTH_SHORT).show()
            }
        })
    }

}
//    private fun fetchUserImages(uid: String) {
//        val uploadsRef = database.reference.child("uploads").orderByChild("s").equalTo(uid)
//
//        uploadsRef.addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                val imageList: MutableList<String> = mutableListOf()
//
//                for (snapshot in dataSnapshot.children) {
//                    val imageUrl = snapshot.child("imageUrl").getValue(String::class.java)
//                    imageUrl?.let { imageList.add(it) }
//                }
//
//                // Setelah mendapatkan daftar URL gambar, pasang adapter RecyclerView
//                val recyclerView: RecyclerView = findViewById(R.id.recyProfil)
//                recyclerView.layoutManager = GridLayoutManager(this@UserLainActivity, 3) // Atur layout manager dengan 3 kolom
//                val imageAdapter = ImageUserAdapter(this@UserLainActivity, imageList)
//                recyclerView.adapter = imageAdapter
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {
//                // Handle error saat mengambil data dari Firebase
//                Toast.makeText(this@UserLainActivity, "Failed to read user images.", Toast.LENGTH_SHORT).show()
//            }
//        })
//    }